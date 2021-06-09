package com.chenjinchi.video.until;


import it.sauronsoftware.jave.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JaveEncodeUtil {
    public static File encode(InputStream stream,String filename, Resolution resolution) throws IOException, EncoderException {
        String basename = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);

        File src = File.createTempFile(basename,"."+extension);
        src.deleteOnExit();
        FileUtils.copyInputStreamToFile(stream,src);

        File dst = File.createTempFile(basename+"_"+resolution,"."+extension);
        dst.deleteOnExit();

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        if(resolution==Resolution.RESOLUTION_720P){
            video.setSize(new VideoSize(1280, 720));
        }else if (resolution==Resolution.RESOLUTION_360P){
            video.setSize(new VideoSize(600, 360));
        }else{
            throw new RuntimeException();
        }

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        encoder.encode(src, dst, attrs);

        return dst;
    }
}
