package org.luck.xhga.spring.websocket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hwb
 */
@Slf4j
public abstract class BaseWebSocket<T> {
    /**
     * 存储session
     */
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    protected abstract void handleOnOpen(WebSocketSession session);
    protected abstract void handleOnMessage(WebSocketSession session, T message);
    public void onOpen(WebSocketSession session) {
        log.info("【WebSocket】[onOpen] 连接成功:{}", session.getId());
        this.addSession(session.getId(), session);
        this.handleOnOpen(session);
    }

    /**
     * 接收到客户端消息
     * @param message 消息体: 如:{"type":"MessageCenter"}
     * @param session session
     */
    public void onMessage(WebSocketSession session, String message) {
        log.info("【WebSocket】[onMessage] 接收到消息: \n{}", message);
        if (!session.isOpen()) {
            log.info("【WebSocket】[onMessage] session={}, 不在线", session.getId());
            return;
        }
        T msg = this.getMessage(message);
        this.handleOnMessage(session, msg);
    }

    protected abstract T getMessage(String message);

    public void onClose(WebSocketSession session) {
        boolean removed = this.removeSession(session);
        log.info("【WebSocket】[onClose] sessionId={}, {}", session.getId(), removed?"移除成功":"移除失败");
    }
    public void onError(WebSocketSession session, Throwable error) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        String sessionId = session.getId();
        log.info("【WebSocket】[onError] sessionId={}, 异常信息={}", sessionId, error.getMessage());
        // 移出用户
        this.removeSession(session);
    }

    /**
     * 发送消息
     * @param session session
     * @param msg 消息
     */
    protected void sendMessage(WebSocketSession session, TextMessage msg) {
        try {
            session.sendMessage(msg);
        } catch (Exception e) {
            this.removeSession(session);
        }
    }

    protected void addSession(String sid, WebSocketSession session) {
        log.info("[addSession] sid={}", sid);
        sessionMap.put(sid, session);
    }

    protected boolean removeSession(WebSocketSession session) {
        return this.removeSession(session.getId());
    }
    protected boolean removeSession(String sid) {
        log.info("[removeSession] sid={}", sid);
        sessionMap.remove(sid);
        return true;
    }
    protected Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }
}
