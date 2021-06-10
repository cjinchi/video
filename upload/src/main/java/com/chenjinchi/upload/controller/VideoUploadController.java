package com.chenjinchi.upload.controller;

import com.chenjinchi.upload.properties.MinIoProperties;
import com.chenjinchi.upload.until.MinIoUtil;
import com.chenjinchi.upload.until.RabbitmqUtil;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
public class VideoUploadController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MinIoProperties minIoProperties;

    @Autowired
    private MinIoUtil minIoUtil;

    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<?> handleVideo(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.indexOf('.') == -1) {
            return ResponseEntity.badRequest().build();
        }
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String objectName = UUID.randomUUID() + "." + fileExtension;

        minIoUtil.putObject(file, minIoProperties.getBucketOriginal(), objectName);

        rabbitmqUtil.sendMessage(System.currentTimeMillis()+" "+objectName);

        System.out.println(objectName);

        return ResponseEntity.ok().body(objectName);
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }


}
