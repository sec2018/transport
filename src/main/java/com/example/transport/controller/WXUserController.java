package com.example.transport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.transport.pojo.WeChatAppLoginReq;
import com.example.transport.pojo.WxUser;
import com.example.transport.service.Constant;
import com.example.transport.service.UserService;
import com.example.transport.util.HmacUtil;
import com.example.transport.util.R;
import com.example.transport.util.TokenGenerator;
import com.example.transport.util.XcxUtils;
import com.example.transport.util.redis.RedisService;
import com.google.gson.Gson;
import com.sun.jndi.toolkit.url.UrlUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RequestMapping("wx")
@Controller
public class WXUserController {

    private static Logger logger = LoggerFactory.getLogger(WXUserController.class);

    private static boolean initialized = false;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    //region 策略1
//    @RequestMapping("wxlogin")
//    @ResponseBody
//    public Map<String,Object> login(WeChatAppLoginReq req)
//    {
//        String code = req.getCode();
//        //获取 session_key 和 openId
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.WX_APP_ID+"&secret="+Constant.WX_APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
//        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
//        {
//            String sessionData = responseEntity.getBody();
//            logger.info("sessionData = "+ sessionData);
//            JSONObject jsonObj = JSON.parseObject(sessionData);
//            String openId = jsonObj.getString("openid");
//            String sessionKey = jsonObj.getString("session_key");
//
//            String signature = HmacUtil.SHA1(req.getRawData()+sessionKey);
//            if(!signature.equals(req.getSignature()))
//            {
//                logger.info(" req signature="+req.getSignature());
//                logger.info(" java signature="+signature);
//            }
//            byte[] resultByte = null;
//            try {
//                resultByte = decrypt(Base64.decode(req.getEncryptedData()), Base64.decode(sessionKey), Base64.decode(req.getIv()));
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//                return null;
//            }
//            if(null != resultByte && resultByte.length > 0)
//            {
//                String userInfoStr = "";
//                try {
//                    userInfoStr = new String(resultByte, "UTF-8");
//                } catch (UnsupportedEncodingException e)
//                {
//                    logger.error(e.getMessage());
//                }
//                logger.info("userInfo = "+ userInfoStr);
//                JSONObject userInfoObj = JSON.parseObject(userInfoStr);
//                WxUser userPo = new WxUser();
////                userPo.setName(userInfoObj.getString("nickName"));
////                userPo.setCreatedTime(new Date());
//                userPo.setGender(userInfoObj.getString("gender"));
////                userPo.setIcon(userInfoObj.getString("avatarUrl"));
////                userPo.setLoginId(userInfoObj.getString("unionId"));
////                userPo.setType((short)UserType.WeiXin);
////                userPo.setLoginType(UserType.WeChatApp);
////                userPo.setNation(userInfoObj.getString("city"));
//                userInfoObj.getString("city");
//                userInfoObj.getString("province");
//                userInfoObj.getString("country");
//
//
//                Map<String,Object> data = new HashedMap();
//                data.put("data", 0);
//                return data;
//            }else
//            {
//                return null;
//            }
//        }else
//        {
//            return null;
//        }
//    }
////
//    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
//        initialize();
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
//
//            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
//            byte[] result = cipher.doFinal(content);
//            return result;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
////
//    public static void initialize(){
//        if (initialized) return;
//        Security.addProvider(new BouncyCastleProvider());
//        initialized = true;
//    }
//    //生成iv
//    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
//        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
//        params.init(new IvParameterSpec(iv));
//        return params;
//    }
    //endregion


    //region 策略2
    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @author zhy
     * @param code 调用微信登陆返回的Code
     * @return
     */
    @RequestMapping("wxlogin")
    @ResponseBody
    public R getSessionKeyOropenid(String code, String encryptedData, String iv){
        //微信端登录code值
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";	//请求地址 https://api.weixin.qq.com/sns/jscode2session
        Map<String,String> requestUrlParam = new HashMap<String,String>();
        requestUrlParam.put("appid", Constant.WX_APP_ID);	//开发者设置中的appId
        requestUrlParam.put("secret", Constant.WX_APP_SECRET);	//开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode);	//小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");	//默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        //获取用户信息
        String session_key = jsonObject.getString("session_key");
        JSONObject WxUserInfo = getUserInfo(encryptedData,session_key,iv);
        String openid = WxUserInfo.getString("openId");
        if(openid!=null){
            String sesssiontoken = TokenGenerator.generateValue();
            redisService.set(sesssiontoken, openid+"|"+session_key);
            jsonObject.remove("openid");
            jsonObject.remove("session_key");
            jsonObject.put("token", sesssiontoken);
            WxUser wxUser = userService.getWxUser(openid);
            if(wxUser!=null){
                wxUser.setCountry(WxUserInfo.getString("country"));
                wxUser.setGender(Integer.parseInt(WxUserInfo.getString("gender")));
                wxUser.setProvince(WxUserInfo.getString("province"));
                wxUser.setCity(WxUserInfo.getString("city"));
                wxUser.setAvatarurl(WxUserInfo.getString("avatarUrl"));
                wxUser.setNickname(WxUserInfo.getString("nickName"));
                wxUser.setLanguage(WxUserInfo.getString("language"));
                String time = JSON.parseObject(WxUserInfo.getString("watermark")).getString("timestamp");
                Date timestamp = timeStampToDate(time+"000");
                wxUser.setTimestamp(timestamp);


                boolean flag = userService.updateWxUser(wxUser);
                if(flag){
                    logger.info("更新成功！");
                }
            }else{
                wxUser = new WxUser();
                wxUser.setCountry(WxUserInfo.getString("country"));
                wxUser.setGender(Integer.parseInt(WxUserInfo.getString("gender")));
                wxUser.setProvince(WxUserInfo.getString("province"));
                wxUser.setCity(WxUserInfo.getString("city"));
                wxUser.setAvatarurl(WxUserInfo.getString("avatarUrl"));
                wxUser.setOpenid(WxUserInfo.getString("openId"));
                wxUser.setNickname(WxUserInfo.getString("nickName"));
                wxUser.setLanguage(WxUserInfo.getString("language"));
                boolean flag = userService.insertWxUser(wxUser);
                if(flag){
                    logger.info("插入成功！");
                }
            }
            logger.info(wxUser.toString());
        }
        return R.ok(jsonObject);
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @author zhy
     * @param sessionKey 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData,String sessionKey,String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while(it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static Date timeStampToDate(String timeStamp) {
        Date date = new Date(Long.valueOf(timeStamp));
        return date;
    }
    //endregion

}
