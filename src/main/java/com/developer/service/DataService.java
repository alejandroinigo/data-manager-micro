package com.developer.service;

import com.developer.persistence.entity.PersistenceData;
import com.developer.controller.model.Record;
import com.developer.exception.DataManagerInternalException;
import com.developer.persistence.mapper.DataObjectMapperBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DataService {
    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    @Value("${google.cloud.storage.object}")
    private String publicObjectName;

//    private String input = "{\n" +
//            "  \"output\": [\n" +
//            "    {\n" +
//            "      \"id\": 6690,\n" +
//            "      \"status\": \"COMPLETED\",\n" +
//            "      \"createdOn\": 1543325977000,\n" +
//            "      \"name\": \"gallant_chandrasekhar\",\n" +
//            "      \"description\": \"Etincidunt etincidunt ut voluptatem numquam dolore aliquam dolore.\",\n" +
//            "      \"delta\": 1770\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"id\": 6689,\n" +
//            "      \"status\": \"COMPLETED\",\n" +
//            "      \"createdOn\": \"2018-11-23T14:06:25.000Z\",\n" +
//            "      \"name\": \"vibrant_hypatia\",\n" +
//            "      \"description\": \"Quisquam eius quiquia eius dolor.\",\n" +
//            "      \"delta\": 1273\n" +
//            "    }\n" +
//            "]\n" +
//            "}";

    public List<Record> getRecords() {
//        ObjectMapper objectMapper = new ObjectMapper()
//                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
//        objectMapper.registerModule(new JavaTimeModule());
        ObjectMapper objectMapper = DataObjectMapperBuilder.build();
        try {
            final PersistenceData persistenceData = objectMapper.readValue(GoogleCloudStorageService.downloadPublicObject(bucketName, publicObjectName), new TypeReference<>() {});
//            final PersistenceData persistenceData = objectMapper.readValue(input, new TypeReference<PersistenceData>() {});
            return persistenceData.getOutput();
        } catch (IOException e) {
            throw new DataManagerInternalException("Exception while mapping json storage file to object", e);
        }
    }
}
