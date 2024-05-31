package org.luck.xhga.spring.websocket.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @author hwb
 */
@Slf4j
@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        List<String> protocols = request.getHeaders().get("Sec-WebSocket-Protocol");
        log.info("[CustomHandshakeInterceptor] sec-websocket-protocol:{}", JSON.toJSONString(protocols));
        if (protocols != null && !protocols.isEmpty()) {
            response.getHeaders().set("Sec-WebSocket-Protocol", protocols.get(0));
        }
        attributes.put("LOGIN_USER", "todo");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
