package com.emall_3_afternoon.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;
    public String get(final String key){ //序列化和反序列化 set就是序列化，把string json转换成对象。就是反序列化
        String result = stringRedisTemplate.opsForValue().get(key);
        if (result == null || result.equals(null))
            result = "";
        return result;
    }

    public boolean set(final String key, final String value){
        boolean result = false;
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    public boolean getAndSet(final  String key, final String value){
        boolean result = false;
        try {
            stringRedisTemplate.opsForValue().getAndSet(key, value);
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
