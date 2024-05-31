package org.luck.xhga.spring.websocket.event;

import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * 自定义事件
 * @author hwb
 */
public class MessageBoxEvent extends ApplicationEvent {
    private Set<Long> userIds;

    public MessageBoxEvent(Object source, Set<Long> userIds) {
        super(source);
        this.userIds = userIds;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }
}
