package com.developer.service;

import com.developer.controller.model.Record;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataServiceTest {
    private static final List<Record> recordsNotSorted = new ArrayList<>();
    private static final List<Record> recordsSortedByIdAsc = new ArrayList<>();
    private static final List<Record> recordsSortedByNameDesc = new ArrayList<>();
    private static final Instant instant = Instant.now();

    @BeforeAll
    public static void initVariables() {
        Collections.addAll(recordsNotSorted,
            new Record(2988L, "agitated_galileo", instant.plus(2L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quisquam eius quiquia eius dolor.", 2156L),
            new Record(1389L, "vibrant_hypatia", instant.plus(1L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L),
            new Record(4256L, "quizzical_yalow", instant.plus(4L, ChronoUnit.MINUTES), Record.StatusEnum.CANCELED, "Porro consectetur magnam modi neque sit modi.", 3254L),
            new Record(3589L, "eloquent_davinci", instant.plus(3L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Neque quaerat dolorem tempora numquam magnam etincidunt eius.", 9584L),
            new Record(5985L, "fervent_nightingale", instant.plus(5L, ChronoUnit.MINUTES), Record.StatusEnum.ERROR, "Dolor sed modi non quisquam sed etincidunt voluptatem.", 1289L));
        Collections.addAll(recordsSortedByIdAsc,
            new Record(1389L, "vibrant_hypatia", instant.plus(1L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L),
            new Record(2988L, "agitated_galileo", instant.plus(2L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quisquam eius quiquia eius dolor.", 2156L),
            new Record(3589L, "eloquent_davinci", instant.plus(3L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Neque quaerat dolorem tempora numquam magnam etincidunt eius.", 9584L),
            new Record(4256L, "quizzical_yalow", instant.plus(4L, ChronoUnit.MINUTES), Record.StatusEnum.CANCELED, "Porro consectetur magnam modi neque sit modi.", 3254L),
            new Record(5985L, "fervent_nightingale", instant.plus(5L, ChronoUnit.MINUTES), Record.StatusEnum.ERROR, "Dolor sed modi non quisquam sed etincidunt voluptatem.", 1289L)
        );
        Collections.addAll(recordsSortedByNameDesc,
            new Record(1389L, "vibrant_hypatia", instant.plus(1L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quiquia dolor quaerat dolore etincidunt modi velit.", 6573L),
            new Record(4256L, "quizzical_yalow", instant.plus(4L, ChronoUnit.MINUTES), Record.StatusEnum.CANCELED, "Porro consectetur magnam modi neque sit modi.", 3254L),
            new Record(5985L, "fervent_nightingale", instant.plus(5L, ChronoUnit.MINUTES), Record.StatusEnum.ERROR, "Dolor sed modi non quisquam sed etincidunt voluptatem.", 1289L),
            new Record(3589L, "eloquent_davinci", instant.plus(3L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Neque quaerat dolorem tempora numquam magnam etincidunt eius.", 9584L),
            new Record(2988L, "agitated_galileo", instant.plus(2L, ChronoUnit.MINUTES), Record.StatusEnum.COMPLETED, "Quisquam eius quiquia eius dolor.", 2156L)
        );
    }

    @Test
    public void should_work_sortByIdAsc() {
        List<Record> records = new ArrayList<>(recordsNotSorted);
        records.sort(Comparator.comparing(Record::getId));

        assertEquals(recordsSortedByIdAsc, records);
    }

    @Test
    public void should_work_sortByNameDesc() {
        List<Record> records = new ArrayList<>(recordsNotSorted);
        records.sort(Comparator.comparing(Record::getName).reversed());

        assertEquals(recordsSortedByNameDesc, records);
    }
}
