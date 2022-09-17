package org.luck.xhga.ali.seata.dao.goods;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.luck.xhga.ali.seata.model.goods.Goods;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xhga
 * @since 2021-05-31
 */
@Mapper
@DS("slave_1")
public interface GoodsMapper extends BaseMapper<Goods> {

}
