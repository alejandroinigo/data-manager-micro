package com.developer.controller.converter;

import com.developer.controller.model.StatusEnum;
import org.springframework.core.convert.converter.Converter;


public class StringToEnumStatusConverter implements Converter<String, StatusEnum> {
    @Override
    public StatusEnum convert(String source) {
            return StatusEnum.valueOf(source.toUpperCase());
    }
}