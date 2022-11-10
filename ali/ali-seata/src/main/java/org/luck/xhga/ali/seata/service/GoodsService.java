package org.luck.xhga.ali.seata.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.luck.xhga.ali.seata.dao.goods.GoodsMapper;
import org.luck.xhga.ali.seata.model.goods.Goods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hwb
 * @date 2021/7/5
 */
@Service
@DS("slave_1")
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {
    @Resource
    private GoodsMapper goodsMapper;

    public Goods selectById(String id) {
        return goodsMapper.selectById(id);
    }
}
