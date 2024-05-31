package org.luck.xhga.spring.websocket.publisher;

import org.luck.xhga.spring.websocket.event.MessageBoxEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author hwb
 */
@Component
public class MessageEventPublisher {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(Set<Long> userIds) {
        MessageBoxEvent customEvent = new MessageBoxEvent(this, userIds);
        applicationEventPublisher.publishEvent(customEvent);
    }

}
