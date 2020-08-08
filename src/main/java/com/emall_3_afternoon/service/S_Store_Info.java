package com.emall_3_afternoon.service;

import com.emall_3_afternoon.entity.Store_Info;
import com.emall_3_afternoon.mapper.Store_InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class S_Store_Info {
    @Autowired
    private Store_InfoMapper store_infoMapper;



    public List<Store_Info> getSeller_Store_Info(int b_s_id){
        List<Store_Info> store_infoList=store_infoMapper.getSeller_Store_Info(b_s_id);
        return store_infoList;
    }

    public List<Integer> getSeller_Store_id(int b_s_id){
        List<Integer> store_infoList=store_infoMapper.getSeller_Store_id(b_s_id);
        return store_infoList;
    }
}
