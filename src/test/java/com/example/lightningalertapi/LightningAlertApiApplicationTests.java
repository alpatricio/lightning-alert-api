package com.example.lightningalertapi;

import com.example.lightningalertapi.util.JsonParser;
import com.example.lightningalertapi.util.MapPoint;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootTest
class LightningAlertApiApplicationTests {
    JsonParser jsonParser = new JsonParser();
    String exampleStrike = "{\n" +
            "    \"flashType\": 1,\n" +
            "    \"strikeTime\": 1386285909025,\n" +
            "    \"latitude\": 33.5524951,\n" +
            "    \"longitude\": -94.5822016,\n" +
            "    \"peakAmps\": 15815,\n" +
            "    \"reserved\": \"000\",\n" +
            "    \"icHeight\": 8940,\n" +
            "    \"receivedTime\": 1386285919187,\n" +
            "    \"numberOfSensors\": 17,\n" +
            "    \"multiplicity\": 1\n" +
            "}";
    String exampleAsset = "{\n" +
            "    \"assetName\":\"Dante Street\",\n" +
            "    \"quadKey\":\"023112133033\",\n" +
            "    \"assetOwner\":\"6720\"\n" +
            "  }";



    @Test
    void contextLoads() {

        try {
            jsonParser.parse("assets.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void mapPointShouldComputeQuadkeyFromLongLat() {

        int pixelX = MapPoint.TileSystem.longToPixelX(-94.5822016, 12);
        System.out.println("pixel x:" + pixelX);
        int tileX = MapPoint.TileSystem.pixelToTile(pixelX);
        System.out.println("tileX:" + tileX);

        int pixelY = MapPoint.TileSystem.latToPixelY(33.5524951, 12);
        System.out.println("pixel y:" + pixelY);
        int tileY = MapPoint.TileSystem.pixelToTile(pixelY);
        System.out.println("tileY:" + tileY);

        String expectedQuadKey = MapPoint.TileSystem.tileXYToQuadKey(tileX, tileY, 12);
        System.out.println("quadkey:" + expectedQuadKey);

        JSONObject strike = (JSONObject) JSONSerializer.toJSON(exampleStrike);
        String actualQuadKey = MapPoint.TileSystem.getQuadKey(strike.optDouble("latitude"), strike.optDouble("longitude"), 12);

        Assert.notNull(actualQuadKey,"not null");
    }

}
