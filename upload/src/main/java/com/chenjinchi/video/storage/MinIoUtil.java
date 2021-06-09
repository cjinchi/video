package com.chenjinchi.video.storage;

import com.chenjinchi.video.properties.MinIoProperties;
import com.chenjinchi.video.until.Resolution;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    public void putObject(MultipartFile file,String bucketName ,String objectName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file == null || file.getSize() == 0) {
            throw new RuntimeException();
        }
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

    }

    public void putObject(File file, String bucketName , String objectName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file == null || file.length()==0) {
            throw new RuntimeException();
        }
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(new FileInputStream(file), file.length(), -1)
                        .build());

    }

    public InputStream getObject(String bucketName,String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public Iterable<Result<Item>> listObjects(String bucketName){
        return client.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).build()
        );
    }
}
