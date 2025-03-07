package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class Record {

  public enum StatusEnum {
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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant createdOn;

  @JsonProperty("status")
  private StatusEnum status;

  @JsonProperty("description")
  private String description;

  @JsonProperty("delta")
  private Long delta;

}

