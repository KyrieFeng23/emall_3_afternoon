package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Order_Item_Info;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Order_Item_InfoMapper {
    @Select("select o.*, g.goods_name from order_item_info o " +
            "inner join goods_info g on o.goods_id = g.goods_id where order_id=#{order_id}")
    List<Order_Item_Info> getOrder_Item_InfoList(int order_id);

    @Insert("insert into order_item_info(order_id, order_item_time, goods_id, " +
            "goods_sum, goods_money, goods_property) " +
            "values(#{order_id}, #{order_item_time}, #{goods_id}, " +
            "#{goods_sum}, #{goods_money}, #{goods_property})")
    int insertOrder_Item_Info(Order_Item_Info order_item_info);

    @Update("")
    int updateOrder_Item_Info(Order_Item_Info order_item_info);

}
