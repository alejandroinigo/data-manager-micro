package com.developer.service;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleCloudStorageService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageService.class);

    /**
     * Downloads the object containing the list of records from Google Cloud Storage.
     * It instantiates an anonymous Google Cloud Storage client, which can only access public files.
     *
     * @param bucketName The name of the Google Cloud Storage bucket
     * @param publicObjectName The name of the public object
     * @return The byte array representing the downloaded object
     */
    public static byte[] downloadPublicObject(String bucketName, String publicObjectName) {
        Storage storage = StorageOptions.getUnauthenticatedInstance().getService();
        byte[] content = storage.readAllBytes(bucketName, publicObjectName);
        logger.debug("Downloaded Google Cloud Storage public object {} from bucket name {}", publicObjectName, bucketName);
        return content;
    }
}


