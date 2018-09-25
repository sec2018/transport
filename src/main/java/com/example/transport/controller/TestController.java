package com.example.transport.controller;


import com.example.transport.service.UserService;
import com.example.transport.util.redis.RedisService;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
@Component
public class TestController {
	protected static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private RedisService redisService;
   
    public void set(){
        redisTemplate.opsForValue().set("key1","testValue1");
        System.out.println(redisTemplate.opsForValue().get("key1"));
        Assert.assertEquals("testValue1",redisTemplate.opsForValue().get("key1"));
    }

    public void testobjSerializer() throws InterruptedException {
    	
//       User user = userService.findUserByName("wang");
//       redisTemplate.opsForValue().set("User:wang",user);
//       Assert.assertEquals(true,redisTemplate.hasKey("User:wang"));
//       if (redisTemplate.hasKey("User:wang")) {
//           redisTemplate.delete("User:wang");
//           logger.info("删除用户时候，从缓存中删除用户 >> User:wang");
//       }
//       User user2 = userService.findUserByName("chen");
//       // 放入缓存，并设置缓存时间
//       redisTemplate.opsForValue().set("User:chen", user2, 600, TimeUnit.SECONDS);
    }
    

    public void testRedisService() {
    	redisService.set("wang:phone", "15221275860");
    	System.out.println(redisService.get("wang:phone"));
    }

    @Test
    public void testR() {
        String json = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();

        json = redisService.get("user_"+ip);
        if(json == null) {
            System.out.println("缓存为空：");
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("key","redis缓存");
            json = JSONObject.fromObject(data).toString();
            redisService.set("user_"+ip, json);
        }
        System.out.println(redisService.get("user_"+ip));
    }
}
