package com.ghan.service.impl;


import com.ghan.handler.MqttSendHandler;
import com.ghan.service.MqttSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttSendServiceImpl implements MqttSendService {

    private MqttSendHandler mqttSendHandler;

    @Autowired
    public void setMqttSendHandler(MqttSendHandler mqttSendHandler) {
        this.mqttSendHandler = mqttSendHandler;
    }

    @Override
    public void sendToMqtt(String data) {
        mqttSendHandler.sendToMqtt(data);
    }

    @Override
    public void sendToMqtt(Integer qos, String data) {
        mqttSendHandler.sendToMqtt(qos, data);
    }

    @Override
    public void sendToMqtt(String topic, String data) {
        mqttSendHandler.sendToMqtt(topic, data);
    }

    @Override
    public void sendToMqtt(String topic, Integer qos, String data) {
        mqttSendHandler.sendToMqtt(topic, qos, data);
    }

    @Override
    public void sendToMqtt(String topic, String responseTopic, String correlationData, Integer qos, String data) {
        mqttSendHandler.sendToMqtt(topic, responseTopic, correlationData, qos, data);
    }
}
