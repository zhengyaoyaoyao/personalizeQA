package com.personalize.personalizeqa;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
@SpringBootTest
public class testRedis {
    @Resource
    RedisTemplate redisTemplate;
    @Test
    void testRedis(){
        redisTemplate.opsForValue().set("testKey","testValue");
        String value = (String) redisTemplate.opsForValue().get("testKey");
        System.out.println(value);
    }
}
