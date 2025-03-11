package com.developer.service;

import com.developer.controller.model.OrderEnum;
import com.developer.controller.model.Record;
import com.developer.controller.model.StatusEnum;
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
    private static final int RECORDS_FILTER_SORT_SIZE = 20;
    private static final int PAGE_SIZE = 20;

    @InjectMocks
    RecordProcessingService recordProcessingService;

    @BeforeEach
    public void initVariables() {
        randomRecordsList = createRandomRecordList(RECORDS_FILTER_SORT_SIZE);
    }

    @Test
    public void test_filterByName_when_name_exists_should_find_data() {
        final String filterName = "FILTER_NAME";
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        final int randomRecordIndex = random.nextInt(actualRecords.size() - 2) + 1;

        actualRecords.get(0).setName(filterName);
        actualRecords.get(randomRecordIndex).setName(filterName);
        actualRecords.get(actualRecords.size() - 1).setName(filterName);

        List<Record> recordsFiltered = recordProcessingService.filterByName(actualRecords, filterName);

        assertEquals(3, recordsFiltered.size());
        for (Record record : recordsFiltered) {
            assertEquals(filterName, record.getName());
        }
    }

    @Test
    public void test_filterByName_when_name_contains_and_different_case_should_find_data() {
        final String recordName = "FILTER_NAME_EXTRA_SUFFIX";
        final String filterName = "filter_name";
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        final int randomRecordIndex = random.nextInt(actualRecords.size());
        actualRecords.get(randomRecordIndex).setName(recordName);

        List<Record> recordsFiltered = recordProcessingService.filterByName(actualRecords, filterName);

        assertEquals(1, recordsFiltered.size());
        assertEquals(recordName, recordsFiltered.get(0).getName());
    }

    @Test
    public void test_filterByName_when_name_not_exists_should_not_find_data() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> recordsFiltered = recordProcessingService.filterByName(actualRecords, "NOT_EXISTING_NAME");

        assertEquals(0, recordsFiltered.size());
    }

    @Test
    public void test_filterByStatus_when_status_exists_should_find_data() {
        StatusEnum statusFilter = StatusEnum.CANCELED;
        List<Record> actualRecords = randomRecordsList.stream()
                .peek(record -> record.setStatus(StatusEnum.COMPLETED))
                .collect(Collectors.toList());
        final int randomRecordIndex = random.nextInt(actualRecords.size() - 2) + 1;

        actualRecords.get(0).setStatus(statusFilter);
        actualRecords.get(randomRecordIndex).setStatus(statusFilter);
        actualRecords.get(actualRecords.size() - 1).setStatus(statusFilter);

        List<Record> recordsFiltered = recordProcessingService.filterByStatus(actualRecords, statusFilter);

        assertEquals(3, recordsFiltered.size());
        for (Record record : recordsFiltered) {
            assertEquals(statusFilter, record.getStatus());
        }
    }

    @Test
    public void test_filterByStatus_when_status_not_exists_should_not_find_data() {
        List<Record> actualRecords = randomRecordsList.stream()
                .peek(record -> record.setStatus(StatusEnum.COMPLETED))
                .collect(Collectors.toList());
        List<Record> recordsFiltered = recordProcessingService.filterByStatus(actualRecords, StatusEnum.ERROR);

        assertEquals(0, recordsFiltered.size());
    }

    @Test
    public void test_orderById_when_data_order_asc_should_sort_data_asc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getId));

        recordProcessingService.orderById(actualRecords, OrderEnum.ASC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_orderById_when_data_order_desc_should_sort_data_desc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getId).reversed());

        recordProcessingService.orderById(actualRecords, OrderEnum.DESC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_orderByName_when_data_order_asc_should_sort_data_asc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER));

        recordProcessingService.orderByName(actualRecords, OrderEnum.ASC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_orderByName_when_data_order_desc_should_sort_data_desc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER).reversed());

        recordProcessingService.orderByName(actualRecords, OrderEnum.DESC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_orderByCreatedOn_when_data_order_asc_should_sort_data_asc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getCreatedOn));

        recordProcessingService.orderByCreatedOn(actualRecords, OrderEnum.ASC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_orderByCreatedOn_when_data_order_desc_should_sort_data_desc() {
        List<Record> actualRecords = new ArrayList<>(randomRecordsList);
        List<Record> expectedRecords = new ArrayList<>(randomRecordsList);
        expectedRecords.sort(Comparator.comparing(Record::getCreatedOn).reversed());

        recordProcessingService.orderByCreatedOn(actualRecords, OrderEnum.DESC);

        assertEquals(expectedRecords, actualRecords);
        logTestResult(actualRecords);
    }

    @Test
    public void test_getRecordsPage_when_input_zero_size_should_return_first_page_subset() {
        List<Record> recordsPage;
        List<Record> actualRecords = new ArrayList<>();

        recordsPage = recordProcessingService.getRecordsPage(actualRecords, PAGE_SIZE, 1);

        assertEquals(0, recordsPage.size());
    }

    @Test
    public void test_getRecordsPage_when_input_smaller_page_size_should_return_first_page_subset() {
        testRecordsPagination(5, 1, 5, 1, 5);
    }

    @Test
    public void test_getRecordsPage_when_first_page_should_return_first_page_subset() {
        testRecordsPagination(45, 1, PAGE_SIZE, 1, PAGE_SIZE);
    }

    @Test
    public void test_getRecordsPage_when_second_page_should_return_second_page_subset() {
        testRecordsPagination(45, 2, PAGE_SIZE, PAGE_SIZE + 1,  2 * PAGE_SIZE);
    }

    @Test
    public void test_getRecordsPage_when_last_smaller_page_size_should_return_second_page_subset() {
        testRecordsPagination(45, 3, 5, (2 * PAGE_SIZE) + 1,  45);
    }

    @Test
    public void test_getRecordsPage_when_last_equals_page_size_should_return_second_page_subset() {
        testRecordsPagination(40, 2, PAGE_SIZE, PAGE_SIZE + 1,  2 * PAGE_SIZE);
    }

    private void testRecordsPagination(int actualRecordsSize, int page, int expectedRecordsSize, int expectedFirstRecordId, int expectedLastRecordId) {
        List<Record> actualRecords = createRandomRecordList(actualRecordsSize);
        actualRecords.sort(Comparator.comparing(Record::getId));
        int expectedFirstInputRecordIndex =  Math.max(0, PAGE_SIZE * (page - 1));
        int expectedLastInputRecordIndex =  Math.min(actualRecords.size() - 1, (PAGE_SIZE * page) - 1);

        List<Record> actualRecordsPage = recordProcessingService.getRecordsPage(actualRecords, PAGE_SIZE, page);

        assertEquals(expectedRecordsSize, actualRecordsPage.size());
        assertEquals(actualRecords.get(expectedFirstInputRecordIndex).getId(), actualRecordsPage.get(0).getId());
        assertEquals(expectedFirstRecordId, actualRecordsPage.get(0).getId());
        assertEquals(actualRecords.get(expectedLastInputRecordIndex).getId(), actualRecordsPage.get(expectedRecordsSize - 1).getId());
        assertEquals(expectedLastRecordId, actualRecordsPage.get(expectedRecordsSize - 1).getId());

        logTestResult(actualRecordsPage);
    }

    private static List<Record> createRandomRecordList(int size) {
        List<Record> recordList = new ArrayList<>(size);
        final int startIndex = 0;
        for (int i = 1; i <= size; i++) {
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

    private static void logTestResult(List<Record> records) {
        for (Record record : records) {
            logger.debug("Actual record, Id: {}, Name: {}, CreatedOn: {}", record.getId(), record.getName(), record.getCreatedOn());
        }
    }

}
