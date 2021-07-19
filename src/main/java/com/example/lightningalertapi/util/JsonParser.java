package com.example.lightningalertapi.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonParser {

    public JSONArray parse(String path) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        String jsonTxt = IOUtils.toString( is );

        return (JSONArray) JSONSerializer.toJSON( jsonTxt );
    }
}
