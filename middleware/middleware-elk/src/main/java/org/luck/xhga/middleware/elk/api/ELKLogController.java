package org.luck.xhga.middleware.elk.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GEEX1928
 * @date 2021/6/7
 */
@Slf4j
@RestController
public class ELKLogController {

    @RequestMapping("/elkLog")
    public String elkLog(){
        log.info("你好啊e");
        log.warn("This is a warn message!");
        log.error("This is error message!");
        return "server被调用了！";
    }
}
