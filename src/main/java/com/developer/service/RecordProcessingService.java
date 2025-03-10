package com.developer.service;

import com.developer.controller.model.Record;
import com.developer.controller.model.Record.StatusEnum;
import com.developer.controller.model.SortBy.OrderEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordProcessingService {

     /**
     * Filters the list of records by name
     *
     * @param records The list of records to filter
     * @param name The name to filter by
     * @return A filtered list of Record objects
     */
    public List<Record> filterByName(final List<Record> records, final String name) {
        return records.stream()
                .filter(record -> StringUtils.containsIgnoreCase(record.getName(), name))
                .collect(Collectors.toList());
    }

    /**
     * Filters the list of records by status
     *
     * @param records The list of records to filter
     * @param status The status to filter by
     * @return A filtered list of Record objects
     */
    public List<Record> filterByStatus(final List<Record> records, final StatusEnum status) {
        return records.stream()
                .filter(record -> status.equals(record.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Orders the list of records by Id
     *
     * @param records The list of records to order
     * @param order The order (ascending or descending)
     */
    public void orderById(final List<Record> records, final OrderEnum order) {
        if (order == OrderEnum.ASC) {
            records.sort(Comparator.comparing(Record::getId));
        } else {
            records.sort(Comparator.comparing(Record::getId).reversed());
        }
    }

    /**
     * Orders the list of records by Name
     *
     * @param records The list of records to order
     * @param order The order (ascending or descending)
     */
    public void orderByName(final List<Record> records, final OrderEnum order) {
        if (order == OrderEnum.ASC) {
            records.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER));
        } else {
            records.sort(Comparator.comparing(Record::getName, String.CASE_INSENSITIVE_ORDER).reversed());
        }
    }

    /**
     * Orders the list of records by CreatedOn
     *
     * @param records The list of records to order
     * @param order The order (ascending or descending)
     */
    public void orderByCreatedOn(final List<Record> records, final OrderEnum order) {
        if (order == OrderEnum.ASC) {
            records.sort(Comparator.comparing(Record::getCreatedOn));
        } else {
            records.sort(Comparator.comparing(Record::getCreatedOn).reversed());
        }
    }

    /**
     * Paginates a list of records
     *
     * @param records The list of records to paginate
     * @param pageSize The number of records per page
     * @param page The page number
     * @return A paginated list of Record objects
     */
    public List<Record> getRecordsPage(final List<Record> records, final int pageSize, final int page) {
        final int beginIndex = Math.max(0, pageSize * (page -1));
        final int endIndex = Math.min(records.size(), pageSize * page) + 1;
        return records.subList(beginIndex, endIndex);
    }
}
