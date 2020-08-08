package com.emall_3_afternoon.controller;

import com.emall_3_afternoon.mapper.Address_InfoMapper;
import com.emall_3_afternoon.util.RedisUtil;
import com.emall_3_afternoon.util.SpringUtil;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class C_Test {
    @Autowired
    private RedisUtil redis;
    @Autowired
    private Address_InfoMapper address_infoMapper;

    @RequestMapping("test_getAddressCount")
    @ResponseBody
    public int getAddressCount(){
        return  address_infoMapper.getAddressCount(1);
    }
    @RequestMapping("test_redis")
    @ResponseBody
    public String testRedis(){
        RedisUtil redisUtil = (RedisUtil) SpringUtil.applicationContext.
                getBean("redisUtil");
        redisUtil.set("test_key_name", "value-1234567");
        return redisUtil.get("test_key_name");

    }
}
