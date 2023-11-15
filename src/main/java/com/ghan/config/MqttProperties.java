package com.ghan.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {
    private String url;

    private AuthTypeEnum authType = AuthTypeEnum.NONE;

    private String username;
    private String password;

    private String clientId;

    private Topic defaultSendTopic = new Topic("troila-mqtt_default", 1);

    private List<Topic> receiveTopics;

    private Integer keepAlive = 15;

    private Integer completionTimeout = 3000;

    /**
     * Mqtt 权限认证类型枚举
     */
    @NoArgsConstructor
    public enum AuthTypeEnum {
        NONE, CLIENT_ID, USERNAME, X509
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Topic {
        private String name;

        private Integer qos;
    }

}
