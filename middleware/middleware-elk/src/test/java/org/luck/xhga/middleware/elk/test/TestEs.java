package org.luck.xhga.middleware.elk.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.luck.xhga.middleware.elk.SpringMiddleWareELKApplication;
import org.luck.xhga.middleware.elk.entity.Person;
import org.luck.xhga.middleware.elk.service.EsJpaOperationService;
import org.luck.xhga.middleware.elk.service.EsRestTemplateService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author hwb
 * @date 2021/6/16
 */
@SpringBootTest(classes = SpringMiddleWareELKApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestEs {
    @Resource
    private EsJpaOperationService esJpaOperationService;
    @Resource
    private EsRestTemplateService esRestTemplateService;

    @Test
    public void test() {
        Iterable<Person> all = esJpaOperationService.findAll();
        log.info("jpa result:{}", JSON.toJSONString(all));
        Person person = esRestTemplateService.get(1 + "", Person.class);
        log.info("es result:{}", JSON.toJSONString(person));
    }
}
