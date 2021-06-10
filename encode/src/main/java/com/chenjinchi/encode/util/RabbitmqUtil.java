package com.chenjinchi.encode.util;

import com.chenjinchi.encode.properties.RabbitmqProperties;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
@Configuration
@EnableConfigurationProperties(RabbitmqProperties.class)
public class RabbitmqUtil {
    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    @Bean
    public Channel channel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqProperties.getHost());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(rabbitmqProperties.getQueueName(), false, false, false, null);
        return channel;
    }

    @Autowired
    private Channel channel;

    public String getMessage() throws IOException {
        while (true) {
            GetResponse response = channel.basicGet("encode", true);
            if (response != null) {
                byte[] body = response.getBody();
                return new String(body, StandardCharsets.UTF_8);
            }
        }
    }
}