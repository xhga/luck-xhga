package org.luck.xhga.middleware.elk.service;

import org.luck.xhga.middleware.elk.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author GEEX1928
 * @date 2021/6/15
 */
@Repository
public interface EsJpaOperationService extends ElasticsearchRepository<Person, String> {

}
