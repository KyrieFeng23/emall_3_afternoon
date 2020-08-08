package com.emall_3_afternoon.mapper;
import com.emall_3_afternoon.entity.Goods_Info;
import com.emall_3_afternoon.entity.Store_Info;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Goods_InfoMapper {
    @Insert("insert into goods_info(goods_name, goods_describe, goods_price, " +
            " goods_actual_price,goods_stock, store_id, dictionary_code) values(" +
            "#{goods_name},#{goods_describe},#{goods_price},#{goods_actual_price}," +
            "#{goods_stock},#{store_id},#{dictionary_code})")
    @Options(useGeneratedKeys = true, keyProperty = "goods_id")
    int insertGoods_Info(Goods_Info goods_info);


    @Select("select goods_name from goods_info where goods_id = #{goods_id}")
    String getGoodsName(int goods_id);
    @Select("select store_name from store_info where store_id = #{store_id}")
    String getStoreName(int store_id);
    @Select("select store_id from goods_info where goods_id= #{goods_id}")
    int getStore_id(int goods_id);
    @Select("select goods_actual_price from goods_info where goods_id = #{goods_id}")
    Float getGoodsPrice(int goods_id);
    @Select("select goods_property from order_item_info where order_id = #{order_id}")
    String get_goods_property(int order_id);
//    @Results({//对单表本身不包含list的均可以不写。会自动映射到。
//            /*@Result(id=true,column="goods_property_id",property="goods_property_id"),
//            @Result(column="goods_id",property="goods_id"),
//            @Result(column="goods_name",property="goods_name"),
//            @Result(column="goods_describe",property="goods_describe"),*/
//
//            @Result(column="goods_id",property="goods_property_infoList",
//                    many=@Many(select="com.emall_3_afternoon.mapper.Goods_Property_InfoMapper.getGoods_Property_List",
//                            fetchType= FetchType.EAGER)),
//            @Result(column = "goods_id", property = "goods_photo_path_infoMapperList",
//                    many = @Many(select="com.emall_3_afternoon.mapper." +
//                            "Goods_Photo_Path_InfoMapper.getGoods_Photo_Path_Info_List",
//                            fetchType= FetchType.EAGER))
//    })
    @Select("select * from goods_info where goods_id = #{goods_id}")
    Goods_Info getAGoods_Info(int goods_id);
    @Select("select * from store_info where store_id = #{store_id}")
    Store_Info getAStore_Info(int store_id);

    @Select("select * from goods_info where goods_status = 0")
    List<Goods_Info> getAllUpGoodsList();

//    @Select("select * from goods_info where ")
//    List<Goods_Info> getGoodsList(String field, String field_value, int start, int page_size);

    @Select("select * from goods_info where store_id = #{store_id} and goods_status = 0")
    List<Goods_Info> getGoodsList(int store_id);
    @Select("select * from goods_info where store_id = #{store_id}")
    List<Goods_Info> getAllGoodsList(int store_id);

    @Select("select * from goods_info where goods_id = #{goods_id}")
    @Results({
            @Result(id = true, column = "goods_id", property = "goods_id"),
            @Result(column = "goods_name", property = "goods_name"),
            @Result(column = "goods_describe", property = "goods_describe"),
            @Result(column = "goods_price", property = "goods_price"),
            @Result(column = "goods_actual_price", property = "goods_actual_price"),
            @Result(column = "goods_stock", property = "goods_stock"),
            @Result(column = "goods_begin_time", property = "goods_begin_time"),
            @Result(column = "goods_end_time", property = "goods_end_time"),
            @Result(column = "goods_status", property = "goods_status"),
            @Result(column = "store_id", property = "store_id"),
            @Result(column = "dictionary_code", property = "dictionary_code"),
            //用来存储图片的数据
            @Result(column = "goods_id", property = "goods_photo_path_infoList",
                    many = @Many(select = "com.emall_3_afternoon.mapper.Goods_Photo_Path_InfoMapper.getGoods_Photo_Path_InfoList",
                            fetchType = FetchType.EAGER)),
            //用来存储属性的list的数据
            @Result(column = "goods_id", property = "goods_property_infoList",
                    many = @Many(select = "com.emall_3_afternoon.mapper.Goods_Property_InfoMapper.getGoods_Property_List",
                            fetchType = FetchType.EAGER)),
    })
    Goods_Info getA_EditGoods_Info(int goods_id);


    @Update("update goods_info set goods_name = #{goods_name}, goods_describe = #{goods_describe}," +
            " goods_price = #{goods_price}, goods_actual_price = #{goods_actual_price}," +
            " goods_stock = #{goods_stock} where goods_id = #{goods_id}")
    int updateGoods_Info(Goods_Info goods_info);


    //上架
    @Update("update goods_info set goods_status = 0, goods_begin_time = '${goods_begin_time}' " +
            "where goods_id = ${goods_id}")
    int upStatus(int goods_id, String goods_begin_time);

    //下架
    @Update("update goods_info set goods_status = 1, goods_end_time = '${goods_end_time}' " +
            "where goods_id = ${goods_id}")
    int downStatus(int goods_id, String goods_end_time);
    //删除
    @Delete("delete from goods_info where goods_id = #{goods_id}")
    int deleteGoods_Info(int goods_id);

    @Select("select * from goods_info where goods_name like '%" +
            "${query_value}" + "%' limit #{pos}, #{page_size}")
    List<Goods_Info> getGoodsInfoListByPage(@Param("pos") int pos,
                                            @Param("page_size") int page_size,
                                            @Param("query_value") String query_value);


    //店铺搜索及分页相关
    @Select("select * from goods_info where store_id = #{store_id} and goods_status = 0 and goods_name like '%" +
            "${query_value}" + "%' limit #{pos}, #{page_size}")
    List<Goods_Info> getGoodsInfoListByStoreSearch(@Param("pos") int pos,
                                            @Param("page_size") int page_size,
                                            @Param("query_value") String query_value,
                                            @Param("store_id") int store_id);
    @Select("select count(*) from goods_info where store_id = #{store_id} and goods_status = 0 and goods_name like '%" +
            "${query_value}" + "%'")
    int getCountGoodsInfoListByStoreSearch(@Param("pos") int pos,
                                           @Param("page_size") int page_size,
                                           @Param("query_value") String query_value,
                                           @Param("store_id") int store_id);

    //淘宝搜索及分页相关
    @Select("select * from goods_info where goods_status = 0 and goods_name like '%" +
            "${query_value}" + "%' limit #{pos}, #{page_size}")
    List<Goods_Info> getGoodsInfoListByTaoBaoSearch(@Param("pos") int pos,
                                                   @Param("page_size") int page_size,
                                                   @Param("query_value") String query_value);

    @Select("select count(*) from goods_info where goods_status = 0 and goods_name like '%" +
            "${query_value}" + "%'")
    int getCountGoodsInfoListByTaoBaoSearch(@Param("pos") int pos,
                                           @Param("page_size") int page_size,
                                           @Param("query_value") String query_value);


    @Insert("insert into goods_collect(goods_id,b_s_id) value (#{goods_id},#{b_s_id})")
    int addtocollect(int goods_id,int b_s_id);

    @Select("select distinct goods_id from goods_collect where b_s_id=#{b_s_id}")
    List<Integer> getCollectGoods(int b_s_id);

    @Select("select distinct count(*) from goods_collect where b_s_id=#{b_s_id}")
    int countcollect(int b_s_id);
}
