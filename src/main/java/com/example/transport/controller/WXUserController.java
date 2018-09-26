package com.example.transport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.transport.pojo.WeChatAppLoginReq;
import com.example.transport.pojo.WxUser;
import com.example.transport.service.Constant;
import com.example.transport.service.UserService;
import com.example.transport.util.HmacUtil;
import com.example.transport.util.XcxUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Map;

@RequestMapping("wx")
@Controller
public class WXUserController {

    private static Logger logger = LoggerFactory.getLogger(WXUserController.class);

    private static boolean initialized = false;

    @Autowired
    private UserService userService;

    /**
     * 得到用户信息
     *
     * @param request
     * @param map
     * @return
     */
    @RequestMapping("getwxuser")
    @ResponseBody
    public ModelMap getUser(HttpServletRequest request, ModelMap map){

        String code = request.getParameter("code");

        //获取 session_key 和 openId
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.WX_OPEN_ID+"&secret="+Constant.WX_APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");

        JSONObject shopAddress=null;

        if(StringUtils.isNotEmpty(code))
        {
            String appid = Constant.WX_OPEN_ID;
            String secret = Constant.WX_APP_SECRET;
            shopAddress= XcxUtils.getSessionKeyOropenid(code,appid,secret);
        }
        String openid = shopAddress.getString("openid");
        String sessionKey = shopAddress.getString("sessionKey");
        JSONObject user = XcxUtils.getUserInfo(encryptedData,sessionKey,iv);
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(user.getString("openid"));
        wxUser.setNickname(user.getString("nickName"));
        wxUser.setGender(user.getString("gender"));
        wxUser.setProvince(user.getString("province"));
        wxUser.setCity(user.getString("city"));
        wxUser.setCountry(user.getString("country"));
        wxUser.setAvatarurl(user.getString("avatarUrl"));
        wxUser.setUnionid(user.getString("language"));
        System.out.println(wxUser.toString());
//        if(userService.insertWxUser(wxUser))
//        {
//            System.out.println("添加小程序信息成功");//添加数据库成功
//            map.put("data", 0);
//        }
//        else
//        {
//            System.out.println("添加小程序信息失败");//添加数据库失败
//            map.put("data", 1);
//        }
        return map;
    }


    private String code;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    //获取凭证校检接口

    public String wxlogin()
    {
        //微信的接口
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.WX_OPEN_ID+
                "&secret="+Constant.WX_APP_SECRET+"&js_code="+ code +"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //根据返回值进行后续操作
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            Gson gson = new Gson();
            //解析从微信服务器获得的openid和session_key;
            WxUser weChatSession = gson.fromJson(sessionData,WxUser.class);
            //获取用户的唯一标识
            String openid = weChatSession.getOpenid();
            //获取会话秘钥
//            String session_key = weChatSession.getSession_key();
            //下面就可以写自己的业务代码了
            //最后要返回一个自定义的登录态,用来做后续数据传输的验证
        }

        return null;

    }

    @RequestMapping("wxlogin")
    public Map<String,Object> login(WeChatAppLoginReq req)
    {
        String code = req.getCode();
        //获取 session_key 和 openId
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.WX_OPEN_ID+"&secret="+Constant.WX_APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String>  responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            logger.info("sessionData = "+ sessionData);
            JSONObject jsonObj = JSON.parseObject(sessionData);
            String openId = jsonObj.getString("openid");
            String sessionKey = jsonObj.getString("session_key");

            String signature = HmacUtil.SHA1(req.getRawData()+sessionKey);
            if(!signature.equals(req.getSignature()))
            {
                logger.info(" req signature="+req.getSignature());
                logger.info(" java signature="+signature);
            }
            byte[] resultByte = null;
            try {
                resultByte = decrypt(Base64.decode(req.getEncryptedData()), Base64.decode(sessionKey), Base64.decode(req.getIv()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
            if(null != resultByte && resultByte.length > 0)
            {
                String userInfoStr = "";
                try {
                    userInfoStr = new String(resultByte, "UTF-8");
                } catch (UnsupportedEncodingException e)
                {
                    logger.error(e.getMessage());
                }
                logger.info("userInfo = "+ userInfoStr);
                JSONObject userInfoObj = JSON.parseObject(userInfoStr);
                WxUser userPo = new WxUser();
//                userPo.setName(userInfoObj.getString("nickName"));
//                userPo.setCreatedTime(new Date());
                userPo.setGender(userInfoObj.getString("gender"));
//                userPo.setIcon(userInfoObj.getString("avatarUrl"));
//                userPo.setLoginId(userInfoObj.getString("unionId"));
//                userPo.setType((short)UserType.WeiXin);
//                userPo.setLoginType(UserType.WeChatApp);
//                userPo.setNation(userInfoObj.getString("city"));
                userInfoObj.getString("city");
                userInfoObj.getString("province");
                userInfoObj.getString("country");

                Map<String,Object> data = new HashedMap();
                data.put("data", 0);
                return data;
            }else
            {
                return null;
            }
        }else
        {
            return null;
        }
    }



    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
    //生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
