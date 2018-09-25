package com.example.transport.service;

public interface Constant {
    // 凭证获取（GET）
    public final static String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 用户同意授权,获取code
    public final static String WX_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    // 用户同意授权,回调url
    public final static String WX_REDIRECT_URL = "回调url";
    // 拉去用户信息url
    public final static String WX_SNSAPI_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //微信openId
    public final static String WX_OPEN_ID = "公众好id";
    //微信appSecret
    public final static String WX_APP_SECRET = "appSecret";

}
