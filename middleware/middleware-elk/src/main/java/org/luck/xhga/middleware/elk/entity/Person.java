package org.luck.xhga.middleware.elk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 实体类-人
 * @author hwb
 * @date 2021/6/15
 * Document 属性：
 *  1、indexName 索引名称
 *  2、refreshInterval 索引刷新间隔时间
 *  3、indexStoreType 索引存储类型，一般使用niofs
 *  4、shards 分片数，一般等于elasticsearch节点数
 *  5、replicas 副本数，一般等于shards - 1
 */
@Data
@Document(indexName = "luck-xhga-middleware-elk", indexStoreType = "person")
public class Person {
    @Id
    private int id;
    /**
     * 年龄
     */
    private int age;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身高
     */
    private int height;
    /**
     * 体重
     */
    private int weight;
}
