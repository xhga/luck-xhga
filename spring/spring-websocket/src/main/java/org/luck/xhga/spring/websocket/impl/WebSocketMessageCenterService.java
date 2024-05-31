package org.luck.xhga.spring.websocket.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.luck.xhga.spring.websocket.model.MsgCenterWebSocketMsg;
import org.luck.xhga.spring.websocket.event.MessageBoxEvent;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消息中心实现类
 * @author hwb
 */
@Slf4j
@Component
public class WebSocketMessageCenterService extends BaseWebSocket<MsgCenterWebSocketMsg> implements ApplicationRunner, ApplicationListener<MessageBoxEvent> {

    @Override
    protected void handleOnOpen(WebSocketSession session) {
    }
    @Override
    protected void handleOnMessage(WebSocketSession session, MsgCenterWebSocketMsg message) {
        if (message == null) {
            return;
        }
        Long deptId = message.getDeptId();
        if (deptId == null) {
            return;
        }
        session.getAttributes().put("deptId", deptId);
        this.sendMessage(session, "handleOnMessage");
    }

    @Override
    protected MsgCenterWebSocketMsg getMessage(String message) {
        return JSON.parseObject(message, MsgCenterWebSocketMsg.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 保持session
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            // log.info("【WebSocketMessageCenterService】[run] 开始发送心跳包");
            Collection<WebSocketSession> sessions = super.getSessionMap().values();
            if (CollectionUtils.isEmpty(sessions)) {
                return;
            }
            sessions.forEach(session -> {
                if (!session.isOpen()) {
                    return;
                }
                this.sendMessage(session, "心跳包");
            });
        }, 0, 40, TimeUnit.SECONDS); // 每30秒发送一次心跳包

    }

    @Override
    public void onApplicationEvent(MessageBoxEvent event) {
        log.info("【onApplicationEvent】event={}", JSONObject.toJSONString(event));
        Collection<WebSocketSession> sessions = super.getSessionMap().values();
        if (CollectionUtils.isEmpty(sessions)) {
            log.error("【onApplicationEvent】未找到任何session");
            return;
        }
        if (CollectionUtils.isEmpty(event.getUserIds())) {
            log.error("【onApplicationEvent】未找到任何userId");
            return;
        }
        sessions.forEach(session -> {
            if (!session.isOpen()) {
                log.error("【onApplicationEvent】连接已关闭");
                return;
            }
            this.sendMessage(session, "onApplicationEvent");
        });
    }

    /**
     * 发送消息
     * @param session session
     */
    private void sendMessage(WebSocketSession session, String desc) {
        if (session == null) {
            return;
        }
        TextMessage textMessage = new TextMessage(desc);
        this.sendMessage(session, textMessage);
    }
}
