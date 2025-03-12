package com.developer.controller;

import com.developer.controller.model.*;
import com.developer.controller.model.Record;
import com.developer.service.DataService;
import com.developer.service.RecordProcessingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class RecordController {

    @Autowired
    DataService dataService;

    @Autowired
    RecordProcessingService recordProcessingService;

    /**
     * Retrieves a list of records based on specified filters and pagination
     *
     * @param name Filters records by name
     * @param status Filters records by status
     * @param page Page number for pagination (default: 1)
     * @param pageSize Number of records per page (default: 20, max: 100)
     * @param field Sort by field criteria
     * @param order Order by criteria
     * @return A ResponseEntity containing a list of Record objects
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/records",
            produces = {"application/json"}
    )
    ResponseEntity<RecordPage> getRecords(
            @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @Valid @RequestParam(value = "status", required = false) StatusEnum status,
            @Min(1) @Valid @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @Min(1) @Max(100) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) FieldEnum field,
            @Valid @RequestParam(value = "sortOrder", required = false) OrderEnum order
    ) {
        List<Record> filteredRecordData = filterRecords(dataService.getRecords(), name, status);
        sortRecords(filteredRecordData, field, order);
        final int totalPages = countNumberOfPages(filteredRecordData.size(), pageSize);
        final int totalRecords = filteredRecordData.size();
        RecordPage recordPage = new RecordPage(totalPages, totalRecords, paginateRecords(filteredRecordData, pageSize, page));
        return new ResponseEntity<>(recordPage, HttpStatus.OK);
    }

    /**
     * Filters a list of records based on name and status
     *
     * @param recordData The list of records to filter
     * @param name    The name to filter by
     * @param status  The status to filter by
     * @return The filtered list of records
     */
    private List<Record> filterRecords(final List<Record> recordData, final String name, final StatusEnum status) {
        List<Record> filteredRecordData = new ArrayList<>(recordData);
        if (!StringUtils.isEmpty(name)) {
            filteredRecordData = recordProcessingService.filterByName(filteredRecordData, name);
        }
        if (null != status) {
            filteredRecordData = recordProcessingService.filterByStatus(filteredRecordData, status);
        }
        return filteredRecordData;
    }

    /**
     * Sorts a list of records based on a field and order
     *
     * @param recordData The list of records to sort
     * @param field   The field to sort by
     * @param order   The sorting order (asc or desc)
     */
    private void sortRecords(final List<Record> recordData, final FieldEnum field, final OrderEnum order) {
        if (field == null) {
            recordProcessingService.orderById(recordData, OrderEnum.ASC);
            return;
        }
        switch (field) {
            case ID:
                recordProcessingService.orderById(recordData, order);
                break;
            case NAME:
                recordProcessingService.orderByName(recordData, order);
                break;
            case CREATEDON:
                recordProcessingService.orderByCreatedOn(recordData, order);
                break;
            default:
                recordProcessingService.orderById(recordData, OrderEnum.ASC);
        }
    }

    /**
     * Paginates a list of records
     *
     * @param recordData  The list of records to paginate
     * @param pageSize The number of records per page
     * @param page     The page number
     * @return The paginated list of records
     */
    private List<Record> paginateRecords(final List<Record> recordData, final int pageSize, final int page) {
        return recordProcessingService.getRecordsPage(recordData, pageSize, page);
    }

    /**
     * Count the number of pages
     *
     * @param numberOfRecords The number of records
     * @param pageSize The number of records per page
     * @return The number of pages
     */
    private int countNumberOfPages(int numberOfRecords, int pageSize) {
        return numberOfRecords / pageSize + (numberOfRecords % pageSize == 0 ? 0 : 1);
    }
}
