package com.ghan.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaderMapper;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Configuration
@EnableConfigurationProperties({MqttProperties.class})
public class Mqtt5Configuration {

    private MqttProperties mqttProperties;

    @Autowired
    public void setMqttProperties(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    public MessageChannel mqtt5InboundChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public MessageChannel mqtt5OutboundChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel errorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public String mqttClientId() {
        String clientId = mqttProperties.getClientId();
        if (StringUtils.hasLength(clientId)) {
            return clientId;
        }
        return String.join("_", "mqttx", UUID.randomUUID().toString().replace("-", ""));
    }

    @Bean
    public MqttConnectionOptions mqttConnectionOptions() {
        MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
        if (mqttProperties.getAuthType().equals(MqttProperties.AuthTypeEnum.USERNAME)) {
            connectionOptions.setUserName(mqttProperties.getUsername());
            connectionOptions.setPassword(mqttProperties.getPassword().getBytes());
        }

        connectionOptions.setServerURIs(new String[]{mqttProperties.getUrl()});
        connectionOptions.setKeepAliveInterval(mqttProperties.getKeepAlive());
        // 断线重连，默认没有重新订阅，必须重写 connectComplete 方法
        connectionOptions.setAutomaticReconnect(true);
        connectionOptions.setAutomaticReconnectDelay(1, 5);
        // 是否重新创建 session
        connectionOptions.setCleanStart(false);
        return connectionOptions;

    }

    @Bean
    @ServiceActivator(inputChannel = "mqtt5OutboundChannel")
    public MessageHandler mqtt5Outbound(@Qualifier("mqttConnectionOptions") MqttConnectionOptions mqttConnectionOptions,
                                        @Qualifier("mqttClientId") String mqttClientId) {
        Mqttv5PahoMessageHandler messageHandler = new Mqttv5PahoMessageHandler(mqttConnectionOptions, mqttClientId.substring(0, 12));
        MqttHeaderMapper mqttHeaderMapper = new MqttHeaderMapper();
        mqttHeaderMapper.setOutboundHeaderNames(MqttHeaders.RESPONSE_TOPIC, MqttHeaders.CORRELATION_DATA, MessageHeaders.CONTENT_TYPE);
        messageHandler.setHeaderMapper(mqttHeaderMapper);
        messageHandler.setDefaultTopic(mqttProperties.getDefaultSendTopic().getName());
        messageHandler.setDefaultQos(mqttProperties.getDefaultSendTopic().getQos());
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);
        return messageHandler;
    }

    @Bean
    public MessageProducer mqtt5Inbound(@Qualifier("mqttConnectionOptions") MqttConnectionOptions mqttConnectionOptions,
                                        @Qualifier("mqttClientId") String mqttClientId) {
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
                new Mqttv5PahoMessageDrivenChannelAdapter(mqttConnectionOptions, mqttClientId,
                        mqttProperties.getReceiveTopics().stream()
                                .map(MqttProperties.Topic::getName)
                                .toArray(String[]::new)) {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        // 断线重连后重新订阅主题
                        if (reconnect) {
                            mqttProperties.getReceiveTopics().forEach(topic -> {
                                super.removeTopic(topic.getName());
                                super.addTopic(topic.getName(), topic.getQos());
                            });
                        }
                    }
                };
        messageProducer.setPayloadType(String.class);
        messageProducer.setManualAcks(false);
        messageProducer.setOutputChannel(mqtt5InboundChannel());
        return messageProducer;
    }

}
