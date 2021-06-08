package com.chenjinchi.video.controller;

import com.chenjinchi.video.storage.MinIoUtil;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
public class VideoUploadController {

    // @Autowired
    // private HttpServletRequest request;

    @Autowired
    private MinIoUtil minIoUtil;

    @PostMapping("/video")
    public ResponseEntity<?> handleVideo(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // String path = "test.mp4";
        // String realPath =request.getServletContext().getRealPath(path);
        // System.out.println(realPath);
        // file.transferTo(new File(realPath));
        // System.out.println(file.getSize());
        // System.out.println(System.getProperty("user.dir"));
        // System.out.println(realPath);

        minIoUtil.putObject(file, "1.mp4");
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/video/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getVideo(@PathVariable Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream stream = minIoUtil.getObject("1.mp4");
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Disposition", "attachment; filename=" + id + ".mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .body(IOUtils.toByteArray(stream));
    }
}
