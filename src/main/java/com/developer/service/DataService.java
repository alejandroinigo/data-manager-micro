package com.developer.service;

import com.developer.controller.model.Record;
import com.developer.exception.DataManagerInternalException;
import com.developer.persistence.entity.PersistenceData;
import com.developer.persistence.mapper.DataObjectMapperBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    @Value("${google.cloud.storage.object}")
    private String publicObjectName;

    private List<Record> data = new ArrayList<>();

    @Value("classpath:data/data.json")
    private Resource resourceFile;

    @Value("${dataservice.cloud.enabled:true}")
    public Boolean isDataFromServer;

    /**
     * Retrieves the list of records from the storage server
     *
     * @return A list of Record objects
     */
    public List<Record> getRecords() {
        if (!data.isEmpty()) {
            logger.debug("Get records from cached data");
            return data;
        }
        if (isDataFromServer) {
            data = getServerRecords().getOutput();
        } else {
            data = getLocalRecords().getOutput();
        }
        data.stream()
                .filter(record -> record.getCreatedOn() == null)
                .forEach(record -> record.setCreatedOn(Instant.ofEpochSecond(0)));

        return data;
    }

    /**
     * Retrieves the object containing the list of records from the remote storage server
     *
     * @return A PersistenceData object with server records
     */
    private PersistenceData getServerRecords() {
        ObjectMapper objectMapper = DataObjectMapperBuilder.build();
        try {
            return objectMapper.readValue(GoogleCloudStorageService.downloadPublicObject(bucketName, publicObjectName), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DataManagerInternalException("Exception while mapping json storage file to object", e);
        }
    }

    /**
     * Retrieves the object containing the list of records from the local storage server
     *
     * @return A PersistenceData object with server records
     */
    private PersistenceData getLocalRecords() {
        ObjectMapper objectMapper = DataObjectMapperBuilder.build();
        try {
            logger.debug("Get records from json storage file");
            return objectMapper.readValue(resourceFile.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DataManagerInternalException("Exception while mapping json storage file to object", e);
        }
    }
}
