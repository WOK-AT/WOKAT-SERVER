package com.sopt.wokat.DB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RedisTest {
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisTest() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();        
        vop.set("yellow", "banana");        

        redisTemplate.delete("yellow");
    }
}
