package com.oc.safetynet.alertsapi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oc.safetynet.alertsapi.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.*;

@Repository
public class DataRepository {

    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    public DataRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("classpath:data.json")
    private Resource dataResource;

    @Value("${data.file.path}")
    private String dataFilePath;

    public Data readData() throws IOException {
        objectMapper = new ObjectMapper();
        try (InputStream inputStream = getResourceInputStream()) {
            return objectMapper.readValue(inputStream, Data.class);
        }
    }

    public void writeData(Data data) throws IOException {
        objectMapper = new ObjectMapper();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        logger.info("Writing data to file: {}", dataFilePath);

        try (OutputStream outputStream = new FileOutputStream(dataFilePath)) {
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.writeValue(outputStream, data);
        }
    }

    private InputStream getResourceInputStream() throws IOException {
        if (dataFilePath.startsWith("classpath:")) {
            return dataResource.getInputStream();
        } else {
            return new FileInputStream(dataFilePath);
        }
    }
}
