package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Goods_Photo_Path_Info;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Goods_Photo_Path_InfoMapper {
    @Insert("insert into goods_photo_path_info(goods_id, path_name) " +
            "values(#{goods_id}, #{path_name})")
    int insertGoods_Photo_Path_Info(Goods_Photo_Path_Info goods_photo_path_info);
    @Delete("delete from goods_photo_path_info where goods_id = #{goods_id}")
    int deleteGoods_Photo_Path_Info(int goods_id);

    @Select("select * from goods_photo_path_info where goods_id = #{goods_id}")
    List<Goods_Photo_Path_Info> getGoods_Photo_Path_InfoList(int goods_id);

    @Select("select path_name from goods_photo_path_info where goods_id = #{goods_id} limit 1")
    String get_A_Goods_Photo_Path(int goods_id);

    @Select("select * from goods_photo_path_info where goods_id = #{goods_id}")
    List<Goods_Photo_Path_Info> get_All_Goods_Photo_Path(int goods_id);
}
