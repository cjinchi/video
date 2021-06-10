package com.chenjinchi.download.controller;

import com.chenjinchi.download.properties.MinIoProperties;
import com.chenjinchi.download.util.MinIoUtil;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class VideoDownloadController {
    @Autowired
    private MinIoUtil minIoUtil;

    @Autowired
    private MinIoProperties minIoProperties;

    @GetMapping(value = "/video/{objectName}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getVideo(@RequestParam(name = "resolution", required = false) String resolution, @PathVariable String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException {
        String bucketName = minIoProperties.getBucketOriginal();
        if (resolution != null) {
            if ("360p".equals(resolution)) {
                bucketName = minIoProperties.getBucket360();
            } else if ("720p".equals(resolution)) {
                bucketName = minIoProperties.getBucket720();
            }
        }
        InputStream stream = minIoUtil.getObject(bucketName, objectName);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + objectName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(IOUtils.toByteArray(stream));
    }

    @GetMapping("/stream/{videoName}")
    public ModelAndView video(@RequestParam(name = "resolution", required = false) String resolution, @PathVariable String videoName, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("stream");
        mav.addObject("videoName", videoName);
        if (resolution != null) {
            mav.addObject("resolution", resolution);
        } else {
            mav.addObject("resolution", "original");
        }
        return mav;
    }

    @GetMapping("/")
    public ModelAndView home() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ModelAndView mav = new ModelAndView("index");
        Collection<String> videoNames = new ArrayList<>();
        Iterable<Result<Item>> results = minIoUtil.listObjects(minIoProperties.getBucketOriginal());
        if (results!=null){
            for(Result<Item> result:results){
                videoNames.add(result.get().objectName());
            }
        }
        mav.addObject("videoNames",videoNames);
        return mav;
    }
}
