package com.luck.xhga.spring.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.security.SecureRandom;

/**
 * @author hwb
 */
@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttConfig {

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client-id}")
    private String clientId;

    @Value("${spring.mqtt.default-topic:topic}")
    private String defaultTopic;

    @Value("${spring.mqtt.will-topic}")
    private String willTopic;

    @Value("${spring.mqtt.will-content}")
    private String willContent;

    @Value("${spring.mqtt.completion-timeout}")
    private int completionTimeout ;

    public static final String DEFAULT_INPUT_CHANNEL = "defaultInputChannel";
    public static final String DEFAULT_OUTPUT_CHANNEL = "defaultOutputChannel";
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        // MQTT的连接设置
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // 设置连接的用户名
        mqttConnectOptions.setUserName(username);
        // 设置连接的密码
        mqttConnectOptions.setPassword(password.toCharArray());
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
        // 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
        mqttConnectOptions.setCleanSession(true);
        // 设置发布端地址,多个用逗号分隔, 如:tcp://111:1883,tcp://222:1883
        // 当第一个111连接上后,222不会在连,如果111挂掉后,重试连111几次失败后,会自动去连接222
        mqttConnectOptions.setServerURIs(hostUrl.split(","));
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(20);
        mqttConnectOptions.setAutomaticReconnect(true);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        mqttConnectOptions.setWill(willTopic, willContent.getBytes(), 2, false);
        mqttConnectOptions.setMaxInflight(1000000);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * @desc 发送通道
     * @date 2021/3/16
     */
    @Bean
    public MessageChannel defaultOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel defaultInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        //clientId每个连接必须唯一,否则,两个相同的clientId相互挤掉线
        String clientIdStr = clientId + System.currentTimeMillis();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientIdStr, mqttClientFactory(), defaultTopic);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(defaultInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = DEFAULT_OUTPUT_CHANNEL)
    public MessageHandler mqttOutbound() {
        // clientId每个连接必须唯一,否则,两个相同的clientId相互挤掉线
        String clientIdStr = clientId + System.currentTimeMillis();
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientIdStr, mqttClientFactory());
        // async如果为true，则调用方不会阻塞。而是在发送消息时等待传递确认。默认值为false（发送将阻塞，直到确认发送)
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);
        messageHandler.setDefaultTopic(defaultTopic);
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }



}
