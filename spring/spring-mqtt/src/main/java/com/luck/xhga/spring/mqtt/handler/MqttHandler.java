package com.luck.xhga.spring.mqtt.handler;

import com.luck.xhga.spring.mqtt.config.MqttConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 */
@Slf4j
@Component
public class MqttHandler {

    @ServiceActivator(inputChannel = MqttConfig.DEFAULT_INPUT_CHANNEL)
    public void consumeMessage(@Header("mqtt_receivedTopic") String topic, Message<String> message) {
        log.info("【MQTT-CONSUME消息】 topic={}, msg={}", topic, message.getPayload());
    }

}