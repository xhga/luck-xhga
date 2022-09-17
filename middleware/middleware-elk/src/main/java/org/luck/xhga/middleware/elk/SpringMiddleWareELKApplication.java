package org.luck.xhga.middleware.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

/**
 * @author GEEX1928
 * @date 2021/6/7
 */
@SpringBootApplication
@EnableReactiveElasticsearchRepositories
public class SpringMiddleWareELKApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMiddleWareELKApplication.class, args);
    }
}
