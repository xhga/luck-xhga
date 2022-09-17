package org.luck.xhga.ali.seata.dao.order;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.luck.xhga.ali.seata.model.order.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xhga
 * @since 2021-05-31
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
