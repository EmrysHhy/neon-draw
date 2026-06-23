package com.emrys.lotterysystem.redis;

import com.emrys.lotterysystem.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void test(){
        stringRedisTemplate.opsForValue().set("test", "test");
        String test = stringRedisTemplate.opsForValue().get("test");
        System.out.println(test);
    }

    @Autowired
    RedisUtil redisUtil;
    @Test
    public void testRedisUtil(){
        boolean a = redisUtil.set("test2", "value2");
        System.out.println(a);
        boolean has = redisUtil.hasKey("test2");
        System.out.println(has);
        String test2 = redisUtil.get("test2");
        System.out.println(test2);
        redisUtil.delete("test2");
        boolean has2 = redisUtil.hasKey("test2");
        System.out.println(has2);
    }
}
