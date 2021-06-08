package com.chenjinchi.video.storage;

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
}
