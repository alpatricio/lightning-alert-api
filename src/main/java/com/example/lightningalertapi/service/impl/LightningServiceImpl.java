package com.example.lightningalertapi.service.impl;

import com.example.lightningalertapi.model.Asset;
import com.example.lightningalertapi.service.LightningService;
import com.example.lightningalertapi.util.JsonParser;
import com.example.lightningalertapi.util.MapPoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class LightningServiceImpl implements LightningService {

    @Value("${asset.config.json}")
    String assetsConfig;

    Map<String, Asset> assets = new HashMap<>();
    Map<String, Asset> assetsTemp = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        JsonParser jsonParser = new JsonParser();
        JSONArray arr = jsonParser.parse(assetsConfig);
        for (int i = 0; i < arr.size(); i++) {
            JSONObject job = arr.getJSONObject(i);
            assets.put(job.optString("quadKey"), new Asset(job));
        }
    }

    @Override
    public List<String> lightningAlert(MultipartFile file) throws IOException, JSONException {
        //stream input
        assetsTemp = new HashMap<>(assets);
        List<String> notifs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        while (reader.ready()) {
            String line = reader.readLine();
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
        }
        return notifs;
    }
}
