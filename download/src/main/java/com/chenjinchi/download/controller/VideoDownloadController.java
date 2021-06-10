package com.chenjinchi.download.controller;

import com.chenjinchi.download.properties.MinIoProperties;
import com.chenjinchi.download.util.MinIoUtil;
import io.minio.errors.*;
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

    // @GetMapping("/play")
    // @ResponseBody
    // public void playVideo(HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    //     response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    //     response.setHeader("Content-Disposition", "attachment; filename=7a011f3f-547d-43d8-8443-713e20bcf555.mp4");
    //     InputStream stream = minIoUtil.getObject(minIoProperties.getBucketOriginal(),"7a011f3f-547d-43d8-8443-713e20bcf555.mp4");
    //     IOUtils.copy(stream,response.getOutputStream());
    //     response.flushBuffer();
    // }

    @GetMapping("/stream/{videoName}")
    public ModelAndView video(@RequestParam(name = "resolution", required = false) String resolution, @PathVariable String videoName, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("videoName", videoName);
        if (resolution != null) {
            mav.addObject("resolution", resolution);
        } else {
            mav.addObject("resolution", "original");
        }
        return mav;
    }
}
