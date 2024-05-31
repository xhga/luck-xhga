package org.luck.xhga.spring.websocket.handle;

import lombok.extern.slf4j.Slf4j;
import org.luck.xhga.spring.websocket.impl.WebSocketMessageCenterService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

/**
 * 消息中心实现类
 * @author hwb
 */
@Slf4j
@Component
public class WebSocketMessageCenterHandler extends TextWebSocketHandler {
    @Resource
    private WebSocketMessageCenterService service;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        service.onOpen(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        service.onMessage(session, message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        service.onError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        service.onClose(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
