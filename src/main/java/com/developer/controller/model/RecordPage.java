package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordPage {

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("totalRecords")
    private Integer totalRecords;

    @JsonProperty("items")
    private List<Record> items;
}
