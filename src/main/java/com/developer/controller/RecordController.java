package com.developer.controller;

import com.developer.controller.model.FieldEnum;
import com.developer.controller.model.OrderEnum;
import com.developer.controller.model.Record;
import com.developer.controller.model.StatusEnum;
import com.developer.service.DataService;
import com.developer.service.RecordProcessingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    ResponseEntity<List<Record>> getRecords(
            @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @Valid @RequestParam(value = "status", required = false) StatusEnum status,
            @Min(1) @Valid @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @Min(1) @Max(100) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(value = "field", required = false) FieldEnum field,
            @Valid @RequestParam(value = "order", required = false) OrderEnum order
    ) {
        List<Record> filteredRecords = filterRecords(dataService.getRecords(), name, status);
        sortRecords(filteredRecords, field, order);
        return new ResponseEntity<>(paginateRecords(filteredRecords, pageSize, page), HttpStatus.OK);
    }

    /**
     * Filters a list of records based on name and status
     *
     * @param records The list of records to filter
     * @param name    The name to filter by
     * @param status  The status to filter by
     * @return The filtered list of records
     */
    private List<Record> filterRecords(final List<Record> records, final String name, final StatusEnum status) {
        List<Record> filteredRecords = new ArrayList<>(records);
        if (!StringUtils.isEmpty(name)) {
            filteredRecords = recordProcessingService.filterByName(filteredRecords, name);
        }
        if (null != status) {
            filteredRecords = recordProcessingService.filterByStatus(filteredRecords, status);
        }
        return filteredRecords;
    }

    /**
     * Sorts a list of records based on a field and order
     *
     * @param records The list of records to sort
     * @param field   The field to sort by
     * @param order   The sorting order (asc or desc)
     */
    private void sortRecords(final List<Record> records, final FieldEnum field, final OrderEnum order) {
        if (field == null) {
            recordProcessingService.orderById(records, OrderEnum.ASC);
            return;
        }
        switch (field) {
            case ID:
                recordProcessingService.orderById(records, order);
                break;
            case NAME:
                recordProcessingService.orderByName(records, order);
                break;
            case CREATED_ON:
                recordProcessingService.orderByCreatedOn(records, order);
                break;
            default:
                recordProcessingService.orderById(records, OrderEnum.ASC);
        }
    }

    /**
     * Paginates a list of records
     *
     * @param records  The list of records to paginate
     * @param pageSize The number of records per page
     * @param page     The page number
     * @return The paginated list of records
     */
    private List<Record> paginateRecords(final List<Record> records, final int pageSize, final int page) {
        return recordProcessingService.getRecordsPage(records, pageSize, page);
    }
}
