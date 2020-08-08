package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Address_Info;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Address_InfoMapper {
    @Select("select * from address_info where b_s_id = #{b_s_id} ORDER BY default_status DESC,address_id ASC")
    List<Address_Info> getAddressList(int b_s_id);

    @Select("select * from address_info where b_s_id = #{b_s_id} and default_status = 1")
    Address_Info getDefaultAddress(int b_s_id);

    @Insert(" insert into address_info(b_s_id, address, address_detail, addresssee," +
            " telephone, default_status, postcode) values( " +
            " #{b_s_id}, #{address}, #{address_detail}, #{addresssee}, " +
            " #{telephone}, #{default_status}, #{postcode})")
    int insertAddress(Address_Info address_info);
    @Update("update address_info set default_status = 0 " +
            " where default_status = 1 and b_s_id = #{b_s_id}")
    int updateDefaultStatus(int b_s_id);
    @Update("update address_info set default_status = 1 where address_id = #{address_id}")
    int setDefaultStatus(int address_id);
    @Select("select count(*) as nums from address_info where b_s_id = #{b_s_id}")
    int getAddressCount(int b_s_id);

    @Delete("delete from address_info where address_id = #{address_id}")
    int deleteAddress(int address_id);
    //原
//    @Update("update address_info set default_status = 1 where address_id = " +
//            "(select address_id from address_info where b_s_id = #{b_s_id} limit 1)")
    //改
    @Update("update address_info set default_status = 1 where address_id =(select a.address_id from(select address_id from address_info where b_s_id = 1 limit 1)a)")
    int updateTopOneAddress(int b_s_id);

    @Select("select * from address_info where address_id = #{address_id}")
    Address_Info getAddress(int address_id);
}

