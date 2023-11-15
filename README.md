# spring-integration-mqtt-demo
使用 spring-integration 实现 MQTT 协议的发送和接收
#### 安装教程

1. 例子里的MQTT服务器选用的是EMQX官方提供免费的在线MQTT 5服务器，
参考:[https://www.emqx.com/zh/mqtt/public-mqtt5-broker](https://www.emqx.com/zh/mqtt/public-mqtt5-broker)


#### 相关知识点

##### 消息服务质量

- QoS 0：消息最多传递 1 次，如果当时客户端不可用，则会丢失该消息。
- QoS 1：消息传递至少 1 次。
- QoS 2：消息仅传送 1 次。

##### 主题层级

储备知识：主题名和主题过滤器是大小写敏感的，以前置或后置斜杠 / 区分。

1.  多层通配符#，表示它的父级和任意数量的子层级。
例如：如果客户端订阅主题 sport/tennis/player1/#，它会收到使用下列主题名发布的消息：
    - sport/tennis/player1
    - sport/tennis/player1/ranking
    - sport/tennis/player1/score/wimbledon

2.  单层通配符+，
例如：sport/tennis/+  匹配 sport/tennis/player1 和 sport/tennis/player2 ，但是不匹配 sport/tennis/player1/ranking。


