package org.luck.xhga.spring.websocket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.luck.xhga.spring.websocket.model.BaseWebSocketMsg;

/**
 * 消息体
 * @author hwb
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MsgCenterWebSocketMsg extends BaseWebSocketMsg {
    /**
     * 科室id
     */
    private Long deptId;
}
