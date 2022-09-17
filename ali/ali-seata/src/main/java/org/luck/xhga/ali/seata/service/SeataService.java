package org.luck.xhga.ali.seata.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.luck.xhga.ali.seata.dao.goods.GoodsMapper;
import org.luck.xhga.ali.seata.dao.order.OrderMapper;
import org.luck.xhga.ali.seata.model.goods.Goods;
import org.luck.xhga.ali.seata.model.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author GEEX1928
 * @date 2021/5/31
 */
@Service
public class SeataService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodsService goodsService;

    @GlobalTransactional
    public String handle(String goodsId) {
        Order order = new Order();
        order.setOrderNo(System.currentTimeMillis() + "");
        order.setGoodsId(goodsId);
        orderMapper.insert(order);
        Goods goods = goodsService.selectById(goodsId);
        assert goods.getGoodsCnt() > 0;
        goods.setGoodsCnt(goods.getGoodsCnt() - 1);
        goodsService.updateById(goods);
        return "ok";
    }

}
