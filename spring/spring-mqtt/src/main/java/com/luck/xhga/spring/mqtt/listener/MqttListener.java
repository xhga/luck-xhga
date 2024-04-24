package com.luck.xhga.spring.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttMessageDeliveredEvent;
import org.springframework.integration.mqtt.event.MqttMessageSentEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hwb
 */
@Slf4j
@Component
public class MqttListener {
    /**
     * @desc mqtt连接失败或者订阅失败时,触发MqttConnectionFailedEvent事件
     * @param event
     */
    @EventListener(MqttConnectionFailedEvent.class)
    public void mqttConnectionFailedEvent(MqttConnectionFailedEvent event) {
        log.error("【MQTT-LISTENER】mqttConnectionFailedEvent连接mqtt失败: date={}, error={}", new Date(), event.getCause().getMessage());
    }

    /**
     * @desc 当async和async事件(async-events)都为true时,将发出MqttMessageSentEvent
     * 它包含消息、主题、客户端库生成的消息id、clientId和clientInstance（每次连接客户端时递增）
     * @param event
     */
    @EventListener(MqttMessageSentEvent.class)
    public void mqttMessageSentEvent(MqttMessageSentEvent event) {
        log.info("【MQTT-LISTENER】mqttMessageSentEvent发送信息: date={}, info={}", new Date(), event.toString());
    }

    /**
     * @desc 当async和async事件(async-events)都为true时,将发出MqttMessageDeliveredEvent
     * 当客户端库确认传递时，将发出MqttMessageDeliveredEvent。它包含messageId、clientId和clientInstance，使传递与发送相关。
     * @param event
     */
    @EventListener(MqttMessageDeliveredEvent.class)
    public void mqttMessageDeliveredEvent(MqttMessageDeliveredEvent event) {
        log.info("【MQTT-LISTENER】mqttMessageDeliveredEvent发送成功信息: date={}, info={}", new Date(), event.toString());
    }

    /**
     * @desc 成功订阅到主题，MqttSubscribedEvent事件就会被触发(多个主题,多次触发)
     * @param event
     */
    @EventListener(MqttSubscribedEvent.class)
    public void mqttSubscribedEvent(MqttSubscribedEvent event) {
        log.info("【MQTT-LISTENER】mqttSubscribedEvent订阅成功信息: date={}, info={}", new Date(), event.toString());
    }
}
