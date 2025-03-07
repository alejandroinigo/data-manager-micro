package com.developer.persistence.entity;

import com.developer.controller.model.Record;
import lombok.Data;

import java.util.List;

@Data
public class PersistenceData {
    private List<Record> output;
}
