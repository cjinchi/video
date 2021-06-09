package com.chenjinchi.video.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "min.io")
public class MinIoProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;

    private String bucket360;
    private String bucket720;
    private String bucketOriginal;
}
