package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Field_Dictionary;
import com.emall_3_afternoon.entity.Goods_Property_Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Field_DictionaryMapper {
    @Select("select * from field_dictionary where dictionary_code like '"
            + "${dictionary_code}" + "__'")
    public List<Field_Dictionary> getFieldList(@Param("dictionary_code") String dictionary_code);

    @Select("select field_value from field_dictionary where dictionary_code=#{dictionary_code}")
    public String getFieldValue(String dictionary_code);

    @Select("select property_value from goods_property_info where goods_id=#{goods_id}")
    public List<String> getProperty_Value(int goods_id);

    @Select("select * from goods_property_info where goods_id=#{goods_id}")
    public List<Goods_Property_Info> getGoodsProperty(int goods_id);
}
