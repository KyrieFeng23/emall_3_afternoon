package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Goods_Property_Info;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Goods_Property_InfoMapper {
    @Insert("insert into goods_property_info(goods_id, property_name, property_value) " +
            " values(#{goods_id}, #{property_name}, #{property_value})")
    int insertGoods_Property(Goods_Property_Info goods_property_info);

    @Delete("delete from goods_property_info where goods_id = #{goods_id}")
    int deleteGoods_Property_Info(int goods_id);

    @Select("select * from goods_property_info where goods_id = #{goods_id}")
    List<Goods_Property_Info> getGoods_Property_List(int goods_id);
}
