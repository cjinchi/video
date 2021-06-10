package com.chenjinchi.encode.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JaveEncodeUtil {
    public static File encode(InputStream stream, String filename, Resolution resolution) throws IOException, EncoderException {
        String basename = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);

        File src = File.createTempFile(basename,"."+extension);
        src.deleteOnExit();
        FileUtils.copyInputStreamToFile(stream,src);

        File dst = File.createTempFile(basename+"_"+resolution,"."+extension);
        dst.deleteOnExit();

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        if(resolution==Resolution.RESOLUTION_720P){
            video.setSize(new VideoSize(1280, 720));
        }else if (resolution==Resolution.RESOLUTION_360P){
            video.setSize(new VideoSize(600, 360));
        }else{
            throw new RuntimeException();
        }

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(src), dst, attrs);

        return dst;
    }
}
