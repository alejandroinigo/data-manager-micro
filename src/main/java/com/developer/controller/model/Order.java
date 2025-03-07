package com.developer.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class Order {
  public enum FieldEnum {
    ID("id"),
    NAME("name"),
    CREATED_ON("createdOn");

    private final String value;

    FieldEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

  public enum OrderEnum {
    ASC("asc"),
    DESC("desc");

    private final String value;

    OrderEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

  @JsonProperty("field")
  private FieldEnum field = FieldEnum.ID;
  @JsonProperty("order")
  private OrderEnum order = OrderEnum.ASC;
  @JsonProperty("priority")
  private Integer priority;
}

