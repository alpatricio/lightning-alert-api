package com.example.lightningalertapi.repository.impl;

import com.example.lightningalertapi.model.Asset;
import com.example.lightningalertapi.repository.AssetRepository;
import com.example.lightningalertapi.util.JsonParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AssetRepositoryImpl implements AssetRepository {

    private Map<String, Asset> mappedAssets = new HashMap<>();
    private List<Asset> assets = new ArrayList<>();

    @Value("${asset.config.json}")
    private String assetsConfig;

    @PostConstruct
    public void init() throws IOException {
        JsonParser jsonParser = new JsonParser();
        JSONArray arr = jsonParser.parse(assetsConfig);
        for (int i = 0; i < arr.size(); i++) {
            JSONObject job = arr.getJSONObject(i);
            mappedAssets.put(job.optString("quadKey"), new Asset(job));
            assets.add(new Asset(job));
        }
    }

    public Map<String, Asset> getMappedAssets(){
        return mappedAssets;
    }

    public List<Asset> getAssets(){
        return assets;
    }

    public Asset getAssetByQuadKey(String quadKey){
        return mappedAssets.get(quadKey);
    }
}
