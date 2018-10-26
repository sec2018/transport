package com.example.transport.controller;


import com.example.transport.service.UserService;
import com.example.transport.util.graphicsutils;
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
import java.util.*;


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

    @Test
    public void initChartData() throws Exception{
        List<List<List<String>>> allValue = new ArrayList<>();
        List<String> content1 = Arrays.asList(new String[]{"刘丹丹","15167876589","上海 上海市 浦东新区","周家渡街道上岸农路1000弄"});
        List<String> content2 = Arrays.asList(new String[]{"陆昊阳","15167876589","上海 上海市 浦东新区","周家渡街道上岸农路190弄"});
        List<List<String>> contentArray1 = new ArrayList<>();
        contentArray1.add(content1);
        List<List<String>> contentArray2 = new ArrayList<>();
        contentArray2.add(content2);
        allValue.add(contentArray1);
        allValue.add(contentArray2);

        List<String[]> headTitles = new ArrayList<>();
        String[] h1 = new String[]{"姓名","电话","省市区","详细地址"};
        String[] h2 = new String[]{"姓名","电话","省市区","详细地址"};
        headTitles.add(h1);
        headTitles.add(h2);

        List<String> titles = new ArrayList<>();
        titles.add("收件人信息");
        titles.add("寄件人信息");
        graphicsutils.createImg(allValue,titles,headTitles ,"",4);
    }
}
