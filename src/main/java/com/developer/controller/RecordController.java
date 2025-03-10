package com.developer.controller;

import com.developer.controller.model.SortBy;
import com.developer.controller.model.Record;
import com.developer.controller.model.Record.StatusEnum;
import com.developer.service.DataService;
import com.developer.service.RecordProcessingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @Autowired
    DataService dataService;

    @Autowired
    RecordProcessingService recordProcessingService;

    /**
     * Retrieves a list of records based on specified filters and pagination
     *
     * @param name Filters records by name
     * @param status Filters records by status (multiple values allowed)
     * @param page Page number for pagination (default: 1)
     * @param pageSize Number of records per page (default: 20, max: 100)
     * @param sortBy List of order criteria
     * @return A ResponseEntity containing a list of Record objects
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/records",
            produces = {"application/json"}
    )
    ResponseEntity<List<Record>> getRecords(
            @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @Valid @RequestParam(value = "status", required = false, defaultValue = "") StatusEnum status,
            @Min(1) @Valid @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @Min(1) @Max(100) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) SortBy sortBy
    ) {
        List<Record> filteredRecords = filterRecords(dataService.getRecords(), name, status);
        sortRecords(filteredRecords, sortBy);
        return new ResponseEntity<>(paginateRecords(filteredRecords, pageSize, page), HttpStatus.OK);
    }

    public List<Record> filterRecords(final List<Record> records, final String name, final StatusEnum status) {
        List<Record> filteredRecords = new ArrayList<>(records);
        if (!StringUtils.isEmpty(name)) {
            filteredRecords = recordProcessingService.filterByName(filteredRecords, name);
        }
        if (StatusEnum.EMPTY == status) {
            filteredRecords = recordProcessingService.filterByStatus(filteredRecords, status);
        }
        return filteredRecords;
    }

    public void sortRecords(final List<Record> records, final SortBy sortBy) {
        if (sortBy == null) {
            return;
        }
        switch (sortBy.getField()) {
            case ID:
                recordProcessingService.orderById(records, sortBy.getOrder());
                break;
            case NAME:
                recordProcessingService.orderByName(records, sortBy.getOrder());
                break;
            case CREATED_ON:
                recordProcessingService.orderByCreatedOn(records, sortBy.getOrder());
                break;
            default:
                recordProcessingService.orderById(records, SortBy.OrderEnum.ASC);
        }
    }

    public List<Record> paginateRecords(final List<Record> records, final int pageSize, final int page) {
        return recordProcessingService.getRecordsPage(records, pageSize, page);
    }
}
