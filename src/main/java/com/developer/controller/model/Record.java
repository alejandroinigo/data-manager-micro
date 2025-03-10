package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Record {

    public enum StatusEnum {
        EMPTY(""),
        COMPLETED("COMPLETED"),
        CANCELED("CANCELED"),
        ERROR("ERROR");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @NotNull
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createdOn;

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("delta")
    private Long delta;

}

