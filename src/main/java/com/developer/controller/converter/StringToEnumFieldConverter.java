package com.developer.controller.converter;

import com.developer.controller.model.FieldEnum;
import org.springframework.core.convert.converter.Converter;


public class StringToEnumFieldConverter implements Converter<String, FieldEnum> {
    @Override
    public FieldEnum convert(String source) {
            return FieldEnum.valueOf(source.toUpperCase());
    }
}