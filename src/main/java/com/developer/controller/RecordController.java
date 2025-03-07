package com.developer.controller;

import com.developer.controller.model.Order;
import com.developer.controller.model.Record;
import com.developer.service.DataService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class RecordController {
    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @Autowired
    DataService dataService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/records",
            produces = { "application/json" }
    )
    ResponseEntity<List<Record>> getRecords(
            @Valid @RequestParam(value = "name", required = false) String name,
            @Valid @RequestParam(value = "status", required = false) List<String> status,
            @Min(1)  @Valid @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @Min(1) @Max(100)  @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) List<@Valid Order> sortBy
    ) {
        logger.debug("name {}, status {}, page {}, pageSize {}, sortBy {}", name, status, page, pageSize, sortBy);
        return new ResponseEntity<>(dataService.getRecords(), HttpStatus.OK);
    }

}
