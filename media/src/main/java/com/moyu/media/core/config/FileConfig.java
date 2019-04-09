package com.moyu.media.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.moyu.media")
public class FileConfig {
    public static String FILE_TYPE_IMAGE="image";
    public static String FILE_TYPE_VIDEO="video";
    public static String FILE_TYPE_CUSTOM="customVideo";

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
