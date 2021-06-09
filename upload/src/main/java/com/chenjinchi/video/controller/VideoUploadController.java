package com.chenjinchi.video.controller;

import com.chenjinchi.video.properties.MinIoProperties;
import com.chenjinchi.video.storage.MinIoUtil;
import com.chenjinchi.video.until.JaveEncodeUtil;
import com.chenjinchi.video.until.Resolution;
import io.minio.errors.*;
import it.sauronsoftware.jave.EncoderException;
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
import java.io.File;
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

    @PostMapping("/video")
    @ResponseBody
    public ResponseEntity<?> handleVideo(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, EncoderException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.indexOf('.') == -1) {
            return ResponseEntity.badRequest().build();
        }
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String objectName = UUID.randomUUID()+"."+fileExtension;

        minIoUtil.putObject(file,minIoProperties.getBucketOriginal(),objectName);

        InputStream stream  = minIoUtil.getObject(minIoProperties.getBucketOriginal(),objectName);
        File file720p = JaveEncodeUtil.encode(stream,objectName, Resolution.RESOLUTION_720P);

        stream = minIoUtil.getObject(minIoProperties.getBucketOriginal(),objectName);
        File file360p = JaveEncodeUtil.encode(stream,objectName,Resolution.RESOLUTION_360P);

        minIoUtil.putObject(file720p,minIoProperties.getBucket720(),objectName);
        minIoUtil.putObject(file360p,minIoProperties.getBucket360(),objectName);

        return ResponseEntity.ok().body(objectName);
    }

    @GetMapping(value = "/video/{objectName}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getVideo(@RequestParam(name = "resolution", required = false)String resolution,@PathVariable String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream stream = minIoUtil.getObject(minIoProperties.getBucketOriginal(),objectName);
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Disposition", "attachment; filename=" + objectName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(IOUtils.toByteArray(stream));
    }
}
