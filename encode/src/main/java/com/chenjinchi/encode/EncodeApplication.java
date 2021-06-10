package com.chenjinchi.encode;

import com.chenjinchi.encode.properties.MinIoProperties;
import com.chenjinchi.encode.util.JaveEncodeUtil;
import com.chenjinchi.encode.util.MinIoUtil;
import com.chenjinchi.encode.util.RabbitmqUtil;
import com.chenjinchi.encode.util.Resolution;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.InputStream;

@SpringBootApplication
public class EncodeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EncodeApplication.class, args);
    }

    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @Autowired
    private MinIoUtil minIoUtil;

    @Autowired
    private MinIoProperties minIoProperties;

    private int currentTaskNum = 0;

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        int maxNum = Integer.parseInt(env.getProperty("task.maxNum"));
        while (true) {
            if (currentTaskNum < maxNum) {
                currentTaskNum++;

                String message = rabbitmqUtil.getMessage();
                new Thread() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        System.out.println("Start process " + message);


                        InputStream stream = minIoUtil.getObject(minIoProperties.getBucketOriginal(), message);
                        File file720p = JaveEncodeUtil.encode(stream, message, Resolution.RESOLUTION_720P);
                        minIoUtil.putObject(file720p, minIoProperties.getBucket720(), message);
                        System.out.println(message + " 720p done");

                        stream = minIoUtil.getObject(minIoProperties.getBucketOriginal(), message);
                        File file360p = JaveEncodeUtil.encode(stream, message, Resolution.RESOLUTION_360P);
                        minIoUtil.putObject(file360p, minIoProperties.getBucket360(), message);
                        System.out.println(message + " 360p done");

                        currentTaskNum--;
                    }
                }.start();
            }
        }
    }
}
