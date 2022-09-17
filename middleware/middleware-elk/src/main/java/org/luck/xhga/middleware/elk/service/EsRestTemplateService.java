package org.luck.xhga.middleware.elk.service;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author GEEX1928
 * @date 2021/6/16
 */
@Component
public class EsRestTemplateService {

    /**
     * 通过ElasticsearchRestTemplate操作es
     */
    @Resource
    private ElasticsearchRestTemplate esTemplate;

    public <T> T get(String id, Class<T> cla) {
        return esTemplate.get(1 + "", cla);
    }

}
