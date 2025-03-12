package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    @NotNull
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("createdOn")
    private Instant createdOn;

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("delta")
    private Long delta;

}

