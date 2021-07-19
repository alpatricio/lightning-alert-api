package com.example.lightningalertapi.service.impl;

import com.example.lightningalertapi.model.Asset;
import com.example.lightningalertapi.service.LightningService;
import com.example.lightningalertapi.util.JsonParser;
import com.example.lightningalertapi.util.MapPoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class LightningServiceImpl implements LightningService {

    @Value("${asset.config.json}")
    String assetsConfig;
    Map<String, Asset> assets = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        JsonParser jsonParser = new JsonParser();
        JSONArray arr = jsonParser.parse(assetsConfig);
        for(int i = 0; i < arr.size(); i++){
            JSONObject job = arr.getJSONObject(i);
            assets.put(job.optString("quadKey"), new Asset(job));
        }
    }

    //TODO:
    //exception
    //error handling
        //empty file
        //non empty file malformed lines

    @Override
    public void lightningAlert(MultipartFile file) {
        try {
            //stream input
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            while(reader.ready()){
                String line = reader.readLine();
                JSONObject strike = (JSONObject) JSONSerializer.toJSON(line);

                //ignore flashType 1 : cloud to cloud & flashType 9 : heartbeat
                //accept flashType 0: cloud to ground
                if(strike.optInt("flashType") == 0){
                    int pixelY = MapPoint.TileSystem.latToPixelY(((JSONObject) strike).optDouble("latitude"),12);
                    int pixelX = MapPoint.TileSystem.longToPixelX(((JSONObject) strike).optDouble("longitude"),12);
                    int tileX = MapPoint.TileSystem.pixelToTile(pixelX);
                    int tileY = MapPoint.TileSystem.pixelToTile(pixelY);
                    String quadKey = MapPoint.TileSystem.tileXYToQuadKey(tileX, tileY, 12);
                    if(assets.get(quadKey) !=null ){
                        System.out.println(assets.get(quadKey).getAssetName()+":"+assets.get(quadKey).getAssetOwner());
                        assets.remove(quadKey);//dont notify anymore. remove from list to be notified
                    }

                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    public void loadAssets() throws IOException {
//        JsonParser jsonParser = new JsonParser();
//        JSONArray arr = jsonParser.parse(assetsFilename);
//        for(int i = 0; i < arr.size(); i++){
//            JSONObject job = arr.getJSONObject(i);
//            assets.put(job.optString("quadKey"), new Asset(job));
//        }
//
//    }
}
