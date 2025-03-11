package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Record {

    @NotNull
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant createdOn;

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("delta")
    private Long delta;

}

