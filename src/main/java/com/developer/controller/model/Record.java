package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

  @Min(1L)
  @JsonProperty("createdOn")
  private Long createdOn;

  @JsonProperty("status")
  private StatusEnum status;

  @JsonProperty("description")
  private String description;

  @JsonProperty("delta")
  private Long delta;

}

