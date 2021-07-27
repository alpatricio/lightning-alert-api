package com.example.lightningalertapi.repository;

import com.example.lightningalertapi.model.Asset;

import java.util.List;
import java.util.Map;

public interface AssetRepository {
    public Asset getAssetByQuadKey(String quadKey);
    public List<Asset> getAssets();
    public Map<String, Asset> getMappedAssets();
}
