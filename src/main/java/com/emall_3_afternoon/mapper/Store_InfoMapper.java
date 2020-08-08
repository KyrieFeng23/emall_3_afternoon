package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Store_Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Store_InfoMapper {
    @Select("select * from store_info where store_id = #{store_id}")
    Store_Info getAStoreInfo(int store_id);

    @Select("select * from store_info where b_s_id = #{b_s_id}")
    List<Store_Info> getSeller_Store_Info(int b_s_id);

    @Select("select * from store_id where b_s_id = #{b_s_id}")
    List<Integer> getSeller_Store_id(int b_s_id);
}
