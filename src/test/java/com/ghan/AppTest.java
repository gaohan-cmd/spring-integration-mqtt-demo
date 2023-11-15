package com.ghan;


import com.ghan.service.MqttSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    private MqttSendService mqttSendService;

    @Autowired
    public void setMqttSendService(MqttSendService mqttSendService) {
        this.mqttSendService = mqttSendService;
    }

    @Test
    public void sendMQTTMessage1() {
        mqttSendService.sendToMqtt("ghan/topic", "ghan/response/topic", "响应报文", 0, "请求响应");
    }

}
