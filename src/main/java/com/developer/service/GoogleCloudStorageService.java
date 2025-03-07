package com.developer.service;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class GoogleCloudStorageService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageService.class);

    /**
     * Instantiate an anonymous Google Cloud Storage client, which can only access public files
     *
     * @return Google Cloud Storage public object byte array
     */
    public static byte[] downloadPublicObject(String bucketName, String publicObjectName) {
        Storage storage = StorageOptions.getUnauthenticatedInstance().getService();
//        Blob blob = storage.get(BlobId.of(bucketName, publicObjectName));
//        blob.downloadTo(destFilePath);
        byte[] content = storage.readAllBytes(bucketName, publicObjectName);
        logger.debug("Downloaded Google Cloud Storage public object {} from bucket name {}", publicObjectName, bucketName);
        return content;
    }
}


