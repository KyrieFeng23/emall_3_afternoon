package com.emall_3_afternoon.service;

import com.emall_3_afternoon.entity.Buyer_Seller_Info;
import com.emall_3_afternoon.mapper.Buyer_SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SBuyer_Seller {
    @Autowired
    private Buyer_SellerMapper buyer_sellerMapper;
    public List<Buyer_Seller_Info>getBuyerList(int status){
        return buyer_sellerMapper.getBuyerList(status);
    }

    public Buyer_Seller_Info find_buyer_seller_by_name(String username,String password){
        return buyer_sellerMapper.find_buyer_seller_by_name(username,password);
    }
    public Buyer_Seller_Info find_buyer_seller_by_b_s_id(int b_s_id){
        return buyer_sellerMapper.find_buyer_seller_by_b_s_id(b_s_id);
    }

    public int update_last_login_time(String last_login_time,int b_s_id){
        return buyer_sellerMapper.update_last_login_time(last_login_time,b_s_id);
    }

    public int update_b_s_info(String nickname,String pwd,String b_s_name,int sex,String telephone,String email,String home,String home_detail,int b_s_id){
        return buyer_sellerMapper.update_b_s_info(nickname,pwd,b_s_name,sex,telephone,email,home,home_detail,b_s_id);
    }
}
