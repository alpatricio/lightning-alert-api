package com.example.lightningalertapi.service.impl;

import com.example.lightningalertapi.exception.ServiceException;
import com.example.lightningalertapi.model.Asset;
import com.example.lightningalertapi.repository.AssetRepository;
import com.example.lightningalertapi.service.LightningService;
import com.example.lightningalertapi.util.MapPoint;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LightningServiceImpl implements LightningService {

    public static final String FILE_NOT_IN_JSON = "FILE NOT IN JSON FORMAT";
    public static final String INVALID_FILE = "INVALID FILE";

    @Autowired
    AssetRepository assetRepository;

    @Override
    public List<String> lightningAlert(MultipartFile file) throws ServiceException {
        Map<String, Asset> assetsTemp = new HashMap<>(assetRepository.getMappedAssets());
        List<String> notifs = new ArrayList<>();
        try {
            //stream input
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            while (reader.ready()) {
                processLine(assetsTemp, notifs, reader);
            }
        } catch (IOException ioException) {
            throw new ServiceException(INVALID_FILE);
        }

        return notifs;
    }

    private void processLine(Map<String, Asset> assetsTemp, List<String> notifs, BufferedReader reader) throws IOException {
        String line = reader.readLine();
        try{
            JSONObject strike = (JSONObject) JSONSerializer.toJSON(line);
            //ignore flashType 1 : cloud to cloud & flashType 9 : heartbeat
            //accept flashType 0: cloud to ground
            if (strike.containsKey("flashType") && strike.optInt("flashType") == 0 &&
                    strike.containsKey("latitude") && strike.containsKey("longitude")) {

                String quadKey = MapPoint.TileSystem.getQuadKey(strike.optDouble("latitude"), strike.optDouble("longitude"), 12);
                if (assetsTemp.get(quadKey) != null) {
                    String notif = "lightning alert for " + assetsTemp.get(quadKey).getAssetOwner() + ":" + assetsTemp.get(quadKey).getAssetName();
                    notifs.add(notif);
                    System.out.println(notif);
                    assetsTemp.remove(quadKey);//dont notify anymore. remove from list to be notified
                }
            }
        } catch (JSONException jsonException) {
            throw new ServiceException(FILE_NOT_IN_JSON);
        }
    }
}
