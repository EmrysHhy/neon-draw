package com.emrys.lotterysystem.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@Configuration
public class RedisUtil {
    /**
     * RedisTemplate存储数据的序列化不同,先将被存储的数据转换成字节数组(不可读取),再存储到Redis中,之后读取按字节数组读取
     * StringRedisTemplate直接存放String类型的数据,读取时直接读取String类型数据,不需要转换,将对象存进去就JSON转换
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean set(String key,String value){
        try {
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set error: {}", e.getMessage());
            return false;
        }

    }

    /**
     * 有过期时间的set方法,以
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,String value,Long timeout){
        try {
            stringRedisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set error: {}", e.getMessage());
            return false;
        }

    }

    /**
     * key 不存在时写入并设置过期时间（用于防重复提交）
     */
    public boolean setIfAbsent(String key, String value, Long timeoutSeconds) {
        if (!StringUtils.hasText(key)) {
            return false;
        }
        try {
            Boolean success = stringRedisTemplate.opsForValue()
                    .setIfAbsent(key, value, timeoutSeconds, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(success);
        } catch (Exception e) {
            log.error("RedisUtil setIfAbsent error: {}", e.getMessage());
            return false;
        }
    }


    public String get(String key){
        if(!StringUtils.hasText(key)){
            return null;
        }
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisUtil get error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 删除key,支持批量删除
     * @param key
     * @return
     */
    public boolean delete(String... key){
        if(key == null || key.length == 0){
            log.info("RedisUtil delete error: key is null or empty");
            return false;
        }

        try {
            stringRedisTemplate.delete(Arrays.asList(key));
            return true;
        } catch (Exception e) {
            log.error("RedisUtil delete error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        if(!StringUtils.hasText(key)){
            return false;
        }
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("RedisUtil hasKey error: {}", e.getMessage());
            return false;
        }
    }

}
