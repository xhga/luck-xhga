package org.luck.xhga.middleware.elk.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.luck.xhga.middleware.elk.entity.Person;
import org.luck.xhga.middleware.elk.service.EsJpaOperationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author GEEX1928
 * @date 2021/6/15
 */
@RestController
@RequestMapping("/operation")
@Slf4j
public class ELKOperationController {
    @Resource
    private EsJpaOperationService elkOperationService;

    @PostMapping("/save")
    public String save(@RequestBody Person person) {
        log.info("保存person:{}", JSON.toJSONString(person));
        elkOperationService.save(person);
        return "success";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable String id) {
        log.info("查询person id:{}", id);
        Optional<Person> byId = elkOperationService.findById(id);
        log.info("查询person id:{}, result:{}", id, JSON.toJSONString(byId));
        return "success";
    }

}
