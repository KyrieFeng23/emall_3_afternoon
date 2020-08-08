package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Goods_Info;
import com.emall_3_afternoon.entity.Order_Info;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Order_InfoMapper {
    //这是用来把订单显示在页面上的
    /*
    * many是用来解决订单和订单明细里面的一对多的问题的。
根据column（order_id）和对应的com.emall_4_afternoon.mapper.Order_Item_InfoMapper
.getOrder_Item_InfoListByOrder_id
去找到Order_Item_InfoMapper下面的.getOrder_Item_InfoListByOrder_id接口方法，
然后执行，得到一个List<Order_Item_Info>的数据集返回。
将返回值填充到property对应的order_item_infoList属性。该属性在Order_Info中肯定存在*/
    @Select("select * from order_info o inner join store_info s  on o.store_id = s.store_id " +
            " where o.b_s_id = #{b_s_id} and o.status = #{status}")
    @Results({
            @Result(column = "order_id", property = "order_item_infoList",
                    many = @Many(select =
                            "com.emall_4_afternoon.mapper.Order_Item_InfoMapper.getOrder_Item_InfoListByOrder_id",
                            fetchType = FetchType.EAGER))
    })
    List<Order_Info> getOrder_InfoListByStatus(int b_s_id, int status);


    @Update("update order_info set order_status = 1 , pay_status = 1 ,pay_time = #{pay_time} where order_id = #{order_id}")
    int update_order_status(int order_id,String pay_time);

    @Delete("delete from order_info where order_id=#{order_id}")
    int delete_order(int order_id);
    @Delete("delete from order_item_info where order_id=#{order_id}")
    int delete_orderItem(int order_id);

    @Select("select order_id from order_info where order_no = ${order_no}")
    int getOrder_id_by_Order_no(String order_no);

    @Select("select * from order_info where b_s_id = #{b_s_id}")
    @Results({ //解决订单明细的存储问题。根据order_id来进行查找，调用
            // com.emall_3_afternoon.mapper.Order_ItemMapper接口下的getOrder_Item_InfoList方法。
            //把获取的list<XXXXX>的数据填充到order_item_infoList.
            @Result(column = "order_id", property = "order_item_infoList",
                    many = @Many(select = "com.emall_3_afternoon.mapper.Order_Item_InfoMapper.getOrder_Item_InfoList",
                            fetchType = FetchType.EAGER))
    })
    List<Order_Info> getOrder_InfoList(int b_s_id);

    @Select("select order_id from order_info where b_s_id = ${b_s_id}")
    List<Integer> getOrder_IdList(int b_s_id);

    @Insert("insert into order_info(order_no, order_money, order_time, store_id," +
            "order_status, pay_way, pay_status, pay_time, b_s_id)" +
            "values(#{order_no}, #{order_money}, #{order_time}, #{store_id}, " +
            "#{order_status}, #{pay_way}, #{pay_status}, #{pay_time}, #{b_s_id})")
    int insertOrder_Info(Order_Info order_info);

    @Select("select * from order_info where order_id = #{order_id}")
    Order_Info getAOrder_Info(int order_id);

    @Update("update order_info set order_status = 0, pay_status = 0 where order_id = #{order_id}")
    int cancelOrder(int order_id);


    //订单搜索及分页相关
    @Select("SELECT * FROM order_info where b_s_id = #{b_s_id} and order_id IN("+
            "SELECT order_id from order_item_info where goods_id IN("+
            "select goods_id from goods_info WHERE goods_name like '%" +"${query_value}" + "%')) or order_no='${query_value}' LIMIT #{pos}, #{page_size}")
    @Results({ //解决订单明细的存储问题。根据order_id来进行查找，调用
            // com.emall_3_afternoon.mapper.Order_ItemMapper接口下的getOrder_Item_InfoList方法。
            //把获取的list<XXXXX>的数据填充到order_item_infoList.
            @Result(column = "order_id", property = "order_item_infoList",
                    many = @Many(select = "com.emall_3_afternoon.mapper.Order_Item_InfoMapper.getOrder_Item_InfoList",
                            fetchType = FetchType.EAGER))
    })
    List<Order_Info> getOrderInfoListByOrderSearch(@Param("pos") int pos,
                                                   @Param("page_size") int page_size,
                                                   @Param("query_value") String query_value,
                                                   @Param("b_s_id") int b_s_id);
    @Select("SELECT count(*) FROM order_info where b_s_id = #{b_s_id} and order_id IN("+
            "SELECT order_id from order_item_info where goods_id IN("+
            "select goods_id from goods_info WHERE goods_name like '%" +"${query_value}" + "%')) or order_no='${query_value}'")
    int getCountOrderInfoListByOrderSearch(@Param("pos") int pos,
                                           @Param("page_size") int page_size,
                                           @Param("query_value") String query_value,
                                           @Param("b_s_id") int b_s_id);
}
