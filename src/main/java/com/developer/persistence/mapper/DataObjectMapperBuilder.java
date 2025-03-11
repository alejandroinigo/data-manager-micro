package com.developer.persistence.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

@UtilityClass
public class DataObjectMapperBuilder {

    public static ObjectMapper build() {
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE)
                .configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, Boolean.TRUE)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(READ_DATE_TIMESTAMPS_AS_NANOSECONDS, Boolean.FALSE)
                .configure(FAIL_ON_EMPTY_BEANS, Boolean.FALSE)
                .configure(WRITE_ENUMS_USING_TO_STRING, Boolean.TRUE)
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}

