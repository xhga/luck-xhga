package com.luck.xhga.spring.mqtt;

import com.luck.xhga.spring.mqtt.gateway.MqttGateway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 测试发送信息
 * @author hwb
 */
@Component
public class TestSend implements ApplicationRunner {
    @Resource
    private MqttGateway mqttGateway;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttGateway.sendToMqtt("test", "topic/jianhuyi01");
    }

}
