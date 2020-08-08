package com.emall_3_afternoon.service;

import com.emall_3_afternoon.entity.Field_Dictionary;
import com.emall_3_afternoon.entity.Goods_Property_Info;
import com.emall_3_afternoon.mapper.Field_DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class S_Field_Dictionary {
    @Autowired
    private Field_DictionaryMapper field_dictionaryMapper;
    public List<Field_Dictionary> getFieldList(String dictionary_code){
        return field_dictionaryMapper.getFieldList(dictionary_code);
    }

    public String getField_Value(String dictionary_code){
        return field_dictionaryMapper.getFieldValue(dictionary_code);
    }
    public List<String> getProperty_Value(int goods_id){
        return field_dictionaryMapper.getProperty_Value(goods_id);
    }
    public List<Goods_Property_Info> getGoodsProperty(int goods_id){
        return field_dictionaryMapper.getGoodsProperty(goods_id);
    }
}
