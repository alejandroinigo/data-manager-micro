package com.developer.controller.converter;

import com.developer.controller.model.OrderEnum;
import org.springframework.core.convert.converter.Converter;


public class StringToEnumOrderConverter implements Converter<String, OrderEnum> {
    @Override
    public OrderEnum convert(String source) {
            return OrderEnum.valueOf(source.toUpperCase());
    }
}