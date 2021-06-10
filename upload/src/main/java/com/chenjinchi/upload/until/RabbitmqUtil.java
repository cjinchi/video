package com.chenjinchi.upload.until;


import com.chenjinchi.upload.properties.MinIoProperties;
import com.chenjinchi.upload.properties.RabbitmqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    public void sendMessage(String message) throws IOException {
        channel.basicPublish("", rabbitmqProperties.getQueueName(), null, message.getBytes());
    }
}
