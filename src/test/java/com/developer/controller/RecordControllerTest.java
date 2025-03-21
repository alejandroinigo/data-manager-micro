package com.developer.controller;

import ch.qos.logback.classic.Logger;
import com.developer.controller.model.Record;
import com.developer.controller.model.RecordPage;
import com.developer.controller.model.StatusEnum;
import com.developer.service.DataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("standalone")
public class RecordControllerTest {
    private final Logger logger = (Logger) LoggerFactory.getLogger(RecordControllerTest.class);
    private static final List<Record> recordsResponse = new ArrayList<>();
    private static final List<Record> expectedResponse = new ArrayList<>();
    private static AutoCloseable closeable;
    Instant recordInstant = Instant.now();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockitoBean
    private DataService dataService;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        recordsResponse.clear();

        Collections.addAll(recordsResponse,
                new Record(2988L, "agitated_galileo", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.ERROR, "Quisquam eius quiquia eius dolor.", 2156L),
                new Record(1389L, "vibrant_hypatia", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L),
                new Record(4256L, "quizzical_yalow", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.CANCELED, "Porro consectetur magnam modi neque sit modi.", 3254L)
        );
        expectedResponse.clear();
        Collections.addAll(expectedResponse,
                new Record(1389L, "vibrant_hypatia", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L),
                new Record(2988L, "agitated_galileo", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.ERROR, "Quisquam eius quiquia eius dolor.", 2156L),
                new Record(4256L, "quizzical_yalow", Instant.now().truncatedTo(ChronoUnit.SECONDS), StatusEnum.CANCELED, "Porro consectetur magnam modi neque sit modi.", 3254L)
        );

        when(dataService.getRecords()).thenReturn(recordsResponse);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void test_getRecords_when_empty_url_params_should_find_data() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String actualRecordsResponse = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/records", String.class);
        RecordPage recordPage = new RecordPage(1,3, expectedResponse);
        assertEquals(recordPage, objectMapper.readValue(actualRecordsResponse, RecordPage.class));
    }

    @Test
    void test_getRecords_when_all_url_params_should_find_data() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String urlTemplate = "http://localhost:{0,number,#}/api/records?name={1}&status={2}&page=1&pageSize=20&field=ID&order=ASC";
        String url = MessageFormat.format(urlTemplate, port, expectedResponse.getFirst().getName(), expectedResponse.getFirst().getStatus());
        String actualRecordsResponse = this.testRestTemplate.getForObject(url, String.class);
        RecordPage recordPage = new RecordPage(1,1, Collections.singletonList(expectedResponse.getFirst()));

        assertEquals(recordPage, objectMapper.readValue(actualRecordsResponse, RecordPage.class));
    }
}
