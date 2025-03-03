package com.openclassrooms.springsecurityauth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
@ConfigurationProperties(prefix = "images")
public class ImageProperties {
    private String imageDir;
    private String baseUrl;

    public String getFullImageDir() {
        return "file:" + this.imageDir;
    }

}