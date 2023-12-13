package com.phatnt15.noticemanagement.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type App config.
 */
@Configuration
@ConfigurationProperties("app-config")
@Getter
@Setter
public class AppConfig {

    private String uploadDir;

    private Boolean allowSelfView;

    private Boolean allowMultipleTimesView;

}
