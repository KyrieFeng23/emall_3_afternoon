package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Logistics_Info;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Logistics_InfoMapper {
    @Insert("insert into logistics_info(order_id, link_man, link_telephone, goods_address, " +
            "address_1, address_detail, logistics_status) " +
            "values(#{order_id}, #{link_man}, #{link_telephone}, #{goods_address}, " +
            "#{address_1}, #{address_detail}, #{logistics_status})")
    int insertLogistics_Info(Logistics_Info logistics_info);

    @Select("select * from logistics_info where order_id =#{order_id}")
    @Results({//用来解决物流总表和物流明细一对多的问题。根据logistics_id来进行查找，调用
            // com.emall_3_afternoon.mapper.Logistics_Item_InfoMapper接口下的getLogistics_Item_InfoList方法。
            //把获取的list<Logistics_Item_Info>的数据填充到logistics_item_infoList.下面代码就是这个意思
            @Result(column = "logistics_id", property = "logistics_item_infoList",
                    many = @Many(select = "com.emall_3_afternoon.mapper.Logistics_Item_InfoMapper" +
                            ".getLogistics_Item_InfoList",
                            fetchType = FetchType.EAGER))
    })
    List<Logistics_Info> getLogistics_InfoList(int order_id);

}
