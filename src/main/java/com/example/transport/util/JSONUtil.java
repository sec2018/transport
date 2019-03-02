package com.example.transport.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Entity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
/**
 * Created by WangZJ on 2018/11/19.
 */

/**
 * 实体类和JSON对象之间相互转化（依赖包jackson-all-1.7.6.jar、jsoup-1.5.2.jar）
 * @author wck
 *
 */
public class JSONUtil {
    /**
     * 将json转化为实体POJO
     * @param jsonStr
     * @param obj
     * @return
     */
    public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将实体POJO转化为JSON
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static<T> JSONObject objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            jsonStr =  mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw e;
        }
        return new JSONObject(jsonStr);
    }


    /**
     * List<T> 转 json
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> String listToJson(List<T> ts){
        return JSON.toJSONString(ts);
    }

    /**
     * json 转 List<T>
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> tClass) {
        return JSONArray.parseArray(jsonStr, tClass);
    }
}

