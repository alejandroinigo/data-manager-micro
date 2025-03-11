package com.developer.config;

import com.developer.controller.converter.StringToEnumFieldConverter;
import com.developer.controller.converter.StringToEnumOrderConverter;
import com.developer.controller.converter.StringToEnumStatusConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumStatusConverter());
        registry.addConverter(new StringToEnumFieldConverter());
        registry.addConverter(new StringToEnumOrderConverter());
    }
}