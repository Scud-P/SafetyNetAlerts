package com.oc.safetynet.alertsapi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.*;

@Repository
public class DataRepository {

    private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    private ObjectMapper objectMapper;

    public DataRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("classpath:data.json")
    private Resource dataResource;

    public Data readData() {
        objectMapper = new ObjectMapper();
        try (InputStream inputStream = dataResource.getInputStream()) {
            return objectMapper.readValue(inputStream, Data.class);
        } catch (IOException e) {
            logger.error("Error reading data from resource: {}", e.getMessage());
            return null;
        }
    }
}
