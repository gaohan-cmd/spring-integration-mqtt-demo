package com.ghan;


import com.ghan.service.MqttSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * 当Spring Boot应用程序启动后，它会自动查找并调用实现了CommandLineRunner接口的类的run方法。
 */
@Component
public class MockMessageRunner implements CommandLineRunner {

    private MqttSendService mqttSendService;

    @Autowired
    public void setMqttSendService(MqttSendService mqttSendService) {
        this.mqttSendService = mqttSendService;
    }

    /**
     * 响应式编程是一种基于数据流和变化传播的编程范式。这意味着可以在编程语言中很方便地表达静态（例如数组）或动态（例如事件发生时）的数据流，而相关的计算模型会自动将变化的值通过数据流进行传播。
     * Flux 简单地理解为一个数据流，它可以发出多个元素（包括 0 个）并且可以持续不断地产生新的元素。这些元素可以是任何类型的对象，比如数字、字符串、自定义对象等
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // 创建了一个每隔1秒发出一个递增数字的数据流,会在指定的时间间隔内发出从 0 开始递增的 Long 类型数值
        Flux.interval(Duration.ofSeconds(1))
                .map(tick -> LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                // 通过subscribe方法订阅这个数据流,即上述经过map操作后的数据流
                .subscribe(temp ->
                        // 发送数据到MQTT服务器
                        // 选择手动指定发送主题，而没有使用默认发送主题,mqttSendService.sendToMqtt(payload);
                        // 发送到ghan/topic主题 期待回复到 ghan/response/topic主题
                        mqttSendService.sendToMqtt("ghan/topic",
                                "ghan/response/topic",
                                UUID.randomUUID().toString(),
                                0,
                                String.valueOf(temp)));
        Thread.currentThread().join();
    }
}
