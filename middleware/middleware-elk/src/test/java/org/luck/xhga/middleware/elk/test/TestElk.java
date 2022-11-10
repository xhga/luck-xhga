package org.luck.xhga.middleware.elk.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luck.xhga.middleware.elk.SpringMiddleWareELKApplication;
import org.luck.xhga.middleware.elk.api.ELKLogController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author hwb
 * @date 2021/6/8
 */
@SpringBootTest(classes = SpringMiddleWareELKApplication.class)
@RunWith(SpringRunner.class)
public class TestElk {
    @Resource
    private ELKLogController elkLogController;

    @Test
    public void elkLog() {
        elkLogController.elkLog();
    }
}
