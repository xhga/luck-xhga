package com.luck.xhga.spring.mqtt.gateway;

import com.luck.xhga.spring.mqtt.config.MqttConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MQTT发送网关
 * @author hwb
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttConfig.DEFAULT_OUTPUT_CHANNEL)
public interface MqttGateway {

    void sendToMqtt(String data);

    void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);

    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);

}
