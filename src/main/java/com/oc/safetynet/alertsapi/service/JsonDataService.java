package com.oc.safetynet.alertsapi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class JsonDataService {

    public JSONObject findDataArrays() {
        try {
            String dataString;
            try (InputStream inputStream = getClass().getResourceAsStream("/data.json")) {
                assert inputStream != null;
                dataString = new String(inputStream.readAllBytes());
            }
            return new JSONObject(dataString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
