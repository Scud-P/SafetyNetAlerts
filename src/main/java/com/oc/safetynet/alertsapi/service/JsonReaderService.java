package com.oc.safetynet.alertsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class JsonReaderService {

    private static final Logger logger = LoggerFactory.getLogger(JsonReaderService.class);

    private ObjectMapper objectMapper;

    public JsonReaderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("classpath:data.json")
    private Resource dataResource;

    public Data readData() {
        try (InputStream inputStream = dataResource.getInputStream()) {
            return objectMapper.readValue(inputStream, Data.class);
        } catch (IOException e) {
            logger.error("Error reading data from resource: {}", e.getMessage());
            return null;
        }
    }
}
