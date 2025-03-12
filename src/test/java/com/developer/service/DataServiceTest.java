package com.developer.service;

import com.developer.controller.model.Record;
import com.developer.controller.model.StatusEnum;
import com.developer.exception.DataManagerInternalException;
import com.developer.persistence.entity.PersistenceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {
    private static AutoCloseable closeable;

    @Mock
    private Resource resourceFile;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataService dataService;

    private List<Record> recordData;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Record record = new Record(1389L, "vibrant_hypatia", Instant.ofEpochSecond(1), StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L);
        this.recordData = Collections.singletonList(record);
        PersistenceData persistenceData = new PersistenceData();
        persistenceData.setOutput(this.recordData);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void test_getRecords_when_local_should_return_resource_file() throws IOException {
        dataService.isDataFromServer = false;
        String inputRecordTemplate = "'{'\"output\": ['{'\"id\": 1389, \"status\": \"COMPLETED\", \"createdOn\": {0}, \"name\": \"vibrant_hypatia\", \"description\": \"Quiquia dolor quaerat dolore etincidunt modi velit.\", \"delta\": 6573'}']'}'";
        String inputRecord = MessageFormat.format(inputRecordTemplate, "1000");
        InputStream inputStream = new ByteArrayInputStream(inputRecord.getBytes());
        when(resourceFile.getInputStream()).thenReturn(inputStream);

        List<Record> result = dataService.getRecords();

        assertEquals(recordData, result);
        verify(resourceFile, times(1)).getInputStream();
    }

    @Test
    void test_getRecords_when_local_should_fail_IOException() throws IOException {
        dataService.isDataFromServer = false;
        when(resourceFile.getInputStream()).thenThrow(new IOException("Test Exception"));

        assertThrows(DataManagerInternalException.class, () -> dataService.getRecords());
    }

}