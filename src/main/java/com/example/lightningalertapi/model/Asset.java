package com.example.lightningalertapi.model;

import net.sf.json.JSONObject;

public class Asset {
    private String assetName;
    private String quadKey;
    private String assetOwner;

    public Asset(JSONObject obj){
        this.assetName = (String) obj.get("assetName");
        this.quadKey = (String) obj.get("quadKey");
        this.assetOwner = (String) obj.get("assetOwner");
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getQuadKey() {
        return quadKey;
    }

    public void setQuadKey(String quadKey) {
        this.quadKey = quadKey;
    }

    public String getAssetOwner() {
        return assetOwner;
    }

    public void setAssetOwner(String assetOwner) {
        this.assetOwner = assetOwner;
    }
}
