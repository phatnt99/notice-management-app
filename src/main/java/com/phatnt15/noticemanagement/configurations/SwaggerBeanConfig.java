package com.phatnt15.noticemanagement.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
/*
 Workaround of the issue
 https://github.com/springdoc/springdoc-openapi/issues/833
 Related mixed JSON object and Files in payload
 related API: POST /api/v1/notices
 */
@Configuration
public class SwaggerBeanConfig {

    public SwaggerBeanConfig(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }
}
