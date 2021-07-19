package com.example.lightningalertapi.service.impl;

import com.example.lightningalertapi.model.Asset;
import com.example.lightningalertapi.service.LightningService;
import com.example.lightningalertapi.util.JsonParser;
import com.example.lightningalertapi.util.MapPoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LightningServiceImpl implements LightningService {

    Map<String, Asset> assets = new HashMap<>();

    //TODO:
    //stream input?
    //asset loading best practice
    //exception

    @Override
    public void lightningAlert(MultipartFile file) {
        try {
            //should not be read everytime endpoint is called
            //change to asset loading best practice
            loadAssets();

            //stream input?
            byte[] bytes = file.getBytes();
            String completeData = new String(bytes);
            List<Object> lightningStrikes = Arrays.asList(completeData.split("\n")).stream().map(JSONSerializer::toJSON).collect(Collectors.toList());


            for(Object strike: lightningStrikes){
                int pixelY = MapPoint.TileSystem.latToPixelY(((JSONObject) strike).optDouble("latitude"),12);
                int pixelX = MapPoint.TileSystem.longToPixelX(((JSONObject) strike).optDouble("longitude"),12);
                int tileX = MapPoint.TileSystem.pixelToTile(pixelX);
                int tileY = MapPoint.TileSystem.pixelToTile(pixelY);
                String q = MapPoint.TileSystem.tileXYToQuadKey(tileX, tileY, 12);

                if(assets.get(q) !=null && ((JSONObject) strike).optInt("flashType") == 0){
                    System.out.println(assets.get(q).getAssetName()+":"+assets.get(q).getAssetOwner());
                    assets.remove(q);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadAssets() throws IOException {
        JsonParser jsonParser = new JsonParser();
        JSONArray arr = jsonParser.parse("assets.json");
        for(int i = 0; i < arr.size(); i++){
            JSONObject job = arr.getJSONObject(i);
            assets.put(job.optString("quadKey"), new Asset(job));
        }

    }
}
