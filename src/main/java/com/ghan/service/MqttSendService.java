package com.ghan.service;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface MqttSendService {

    /**
     * Use Default Topic & Default Qos Send Data
     *
     * @param data string
     */
    void sendToMqtt(String data);

    /**
     * Use Default Topic & Custom Qos Send Data
     *
     * @param qos  Custom Qos
     * @param data string
     */
    void sendToMqtt(@Header(MqttHeaders.QOS) Integer qos, String data);

    /**
     * Use Custom Topic & Default Qos Send Data
     *
     * @param topic Custom Topic
     * @param data  string
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String data);

    /**
     * Use Custom Topic & Custom Qos Send Data
     *
     * @param topic Custom Topic
     * @param qos   Custom Qos
     * @param data  string
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer qos, String data);

    /**
     * 表示可以使用消息头来传递参数，也可以使用方法的参数来传递参数
     * @param topic
     * @param responseTopic
     * @param correlationData
     * @param qos
     * @param data
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    @Header(MqttHeaders.RESPONSE_TOPIC) String responseTopic,
                    @Header(MqttHeaders.CORRELATION_DATA) String correlationData,
                    @Header(MqttHeaders.QOS) Integer qos,
                    String data);
}
