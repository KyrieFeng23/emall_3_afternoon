package com.emall_3_afternoon.mapper;

import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Buyer_SellerMapper {
    @Select("select * from buyer_seller_info where status=#{status}")
    List<Buyer_Seller_Info>getBuyerList(int status);

    @Update("update buyer_seller_info set last_login_time ='${last_login_time}' where b_s_id = ${b_s_id}")
    int update_last_login_time(String last_login_time,int b_s_id);

    @Update("update buyer_seller_info set nickname ='${nickname}',pwd ='${pwd}',b_s_name ='${b_s_name}',sex =${sex}," +
            "telephone ='${telephone}',email ='${email}',home ='${home}',home_detail ='${home_detail}' where b_s_id = ${b_s_id}")
    int update_b_s_info(String nickname,String pwd,String b_s_name,int sex,String telephone,String email,String home,String home_detail,int b_s_id);

    @Insert("insert into buyer_seller_info(b_s_name,nickname,sex,icon,telephtne," +
            "home,homedetail,email)" +
            " values(#{b_s_name},#{nickname},#{sex},#{icon},#{telephone}," +
            "#{home},#{home_detail},#{email}")
    int registerBuyer(Buyer_Seller_Info buyer_seller_info);

    @Select("select * from buyer_seller_info where nickname='${username}' and pwd=${password}")
    Buyer_Seller_Info find_buyer_seller_by_name(String username,String password);

    @Select("select * from buyer_seller_info where b_s_id = #{b_s_id}")
    Buyer_Seller_Info find_buyer_seller_by_b_s_id(int b_s_id);
}

