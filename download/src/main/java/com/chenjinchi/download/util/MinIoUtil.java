package com.chenjinchi.download.util;

import com.chenjinchi.download.properties.MinIoProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Configuration
@EnableConfigurationProperties(MinIoProperties.class)
public class MinIoUtil {

    @Autowired
    private MinIoProperties minIoProperties;

    @Bean
    public MinioClient client() {
        return MinioClient.builder()
                .endpoint(minIoProperties.getEndpoint())
                .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                .build();
    }

    @Autowired
    private MinioClient client;

    public InputStream getObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException {
        return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public Iterable<Result<Item>> listObjects(String bucketName){
        return client.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).build()
        );
    }

}