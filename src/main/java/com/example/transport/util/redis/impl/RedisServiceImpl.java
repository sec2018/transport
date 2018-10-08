package com.example.transport.util.redis.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.example.transport.service.Constant;
import com.example.transport.util.redis.RedisService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

	@Resource
    private RedisTemplate<String, ?> redisTemplate;
	
	private String prefix = "trans_";
	private Expiration expire = Constant.expire;//3600秒后数据过期
	
    @Override
    public boolean set(final String key, final String value) {
 
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(prefix + key), serializer.serialize(value),expire,RedisStringCommands.SetOption.SET_IF_ABSENT);
                return true;
            }
        });
        return result;
    }

    @Override
    public String get(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(prefix + key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }
   @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(prefix + key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean remove(final String key) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.del((prefix + key).getBytes());
                return true;
            }
        });
        return result;
    }
 

}
