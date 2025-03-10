package com.developer.service;

import com.developer.controller.model.SortBy;
import com.developer.controller.model.Record;
import com.developer.controller.model.Record.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecordProcessingServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RecordProcessingServiceTest.class);
    private static List<Record> randomRecordsList = new ArrayList<>();
    private static final Instant instant = Instant.now();
    private static final Random random = new Random();
    private static final int TEST_RECORDS_SIZE = 20;

    @InjectMocks
    RecordProcessingService recordProcessingService;

    @BeforeEach
    public void initVariables() {
        randomRecordsList = createRandomRecordList();
    }

    @Test
    public void test_filterByName_when_name_exists_should_find_data() {
        final String filterName = "FILTER_NAME";
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        final int randomRecordIndex = random.nextInt(inputRecords.size() - 2) + 1;

        inputRecords.get(0).setName(filterName);
        inputRecords.get(randomRecordIndex).setName(filterName);
        inputRecords.get(inputRecords.size() - 1).setName(filterName);

        List<Record> recordsFiltered = recordProcessingService.filterByName(inputRecords, filterName);

        assertEquals(3, recordsFiltered.size());
        recordsFiltered.forEach(record -> {
            assertEquals(filterName, record.getName());
        });
    }

    @Test
    public void test_filterByName_when_name_contains_and_different_case_should_find_data() {
        final String recordName = "FILTER_NAME_EXTRA_SUFFIX";
        final String filterName = "filter_name";
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        final int randomRecordIndex = random.nextInt(inputRecords.size());
        inputRecords.get(randomRecordIndex).setName(recordName);

        List<Record> recordsFiltered = recordProcessingService.filterByName(inputRecords, filterName);

        assertEquals(1, recordsFiltered.size());
        assertEquals(recordName, recordsFiltered.get(0).getName());
    }

    @Test
    public void test_filterByName_when_name_not_exists_should_not_find_data() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> recordsFiltered = recordProcessingService.filterByName(inputRecords, "NOT_EXISTING_NAME");

        assertEquals(0, recordsFiltered.size());
    }

    @Test
    public void test_filterByStatus_when_status_exists_should_find_data() {
        StatusEnum statusFilter = StatusEnum.CANCELED;
        List<Record> inputRecords = randomRecordsList.stream()
                .peek(record -> record.setStatus(StatusEnum.COMPLETED))
                .collect(Collectors.toList());
        final int randomRecordIndex = random.nextInt(inputRecords.size() - 2) + 1;

        inputRecords.get(0).setStatus(statusFilter);
        inputRecords.get(randomRecordIndex).setStatus(statusFilter);
        inputRecords.get(inputRecords.size() - 1).setStatus(statusFilter);

        List<Record> recordsFiltered = recordProcessingService.filterByStatus(inputRecords, statusFilter);

        assertEquals(3, recordsFiltered.size());
        recordsFiltered.forEach(record -> {
            assertEquals(statusFilter, record.getStatus());
        });
    }

    @Test
    public void test_filterByStatus_when_status_not_exists_should_not_find_data() {
        List<Record> inputRecords = randomRecordsList.stream()
                .peek(record -> record.setStatus(StatusEnum.COMPLETED))
                .collect(Collectors.toList());
        List<Record> recordsFiltered = recordProcessingService.filterByStatus(inputRecords, StatusEnum.ERROR);

        assertEquals(0, recordsFiltered.size());
    }

    @Test
    public void test_orderById_when_data_order_asc_should_sort_data_asc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getId));

        recordProcessingService.orderById(inputRecords, SortBy.OrderEnum.ASC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderById asc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    @Test
    public void test_orderById_when_data_order_desc_should_sort_data_desc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getId).reversed());

        recordProcessingService.orderById(inputRecords, SortBy.OrderEnum.DESC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderById desc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    @Test
    public void test_orderByName_when_data_order_asc_should_sort_data_asc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER));

        recordProcessingService.orderByName(inputRecords, SortBy.OrderEnum.ASC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderByName asc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    @Test
    public void test_orderByName_when_data_order_desc_should_sort_data_desc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER).reversed());

        recordProcessingService.orderByName(inputRecords, SortBy.OrderEnum.DESC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderByName desc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    @Test
    public void test_orderByCreatedOn_when_data_order_asc_should_sort_data_asc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getCreatedOn));

        recordProcessingService.orderByCreatedOn(inputRecords, SortBy.OrderEnum.ASC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderByCreatedOn asc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    @Test
    public void test_orderByCreatedOn_when_data_order_desc_should_sort_data_desc() {
        List<Record> inputRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getCreatedOn).reversed());

        recordProcessingService.orderByCreatedOn(inputRecords, SortBy.OrderEnum.DESC);

        assertEquals(expectedRecords, inputRecords);
        for(Record record : inputRecords) {
            logger.debug("orderByCreatedOn desc Id {}, Name {}, CreatedOn {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

    private static List<Record> createRandomRecordList() {
        List<Record> recordList = new ArrayList<>(TEST_RECORDS_SIZE);
        final int startIndex = random.nextInt(1000);
        for (int i = 1; i <= TEST_RECORDS_SIZE; i++){
            recordList.add(createRandomRecord((long) (startIndex + i)));
        }
        Collections.shuffle(recordList);
        return recordList;
    }

    private static Record createRandomRecord(Long id) {
        final int alphabeticSuffixSize = random.nextInt(10);
        return new Record(
                id,
                randomAlphabetic(10 + alphabeticSuffixSize),
                instant.plus(random.nextInt(1000 + alphabeticSuffixSize), ChronoUnit.MINUTES),
                randomStatusEnum(),
                randomAlphabetic(10),
                (long) random.nextInt(1000));
    }

    private static StatusEnum randomStatusEnum() {
        final int randomStatusIndex = random.nextInt(StatusEnum.values().length);
        return StatusEnum.values()[randomStatusIndex];
    }

}
