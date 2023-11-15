package com.ghan.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Configuration
public class MqttReceiveHandler {

    @Bean
    // 在Mqtt5Configuration.java中，设定了客户端发送消息给服务器端的输出通道为mqtt5OutboundChannel
    @ServiceActivator(inputChannel = "mqtt5InboundChannel")
    public MessageHandler handlerMqtt5Message() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            //获取消息Topic
            String receivedTopic = (String) headers.get(MqttHeaders.RECEIVED_TOPIC);
            log.info("获取到MQTT服务端的消息的topic :{} ", receivedTopic);
            String responseTopic = (String) headers.get(MqttHeaders.RESPONSE_TOPIC);
            log.info("获取到MQTT服务端的消息的响应主题 :{} ", responseTopic);

            String correlationData = null;
            if (Objects.nonNull(headers.get(MqttHeaders.CORRELATION_DATA))) {
                correlationData = new String((byte[]) headers.get(MqttHeaders.CORRELATION_DATA), StandardCharsets.UTF_8);
                log.info("获取到MQTT服务端的消息关联数据 :{} ", correlationData);
            }
            //获取消息体
            String payload = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
            log.info("获取到MQTT服务端的消息的payload :{} ", payload);
        };
    }

}
