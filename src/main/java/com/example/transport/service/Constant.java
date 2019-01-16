package com.example.transport.service;

import org.springframework.data.redis.core.types.Expiration;

public class Constant {
    // 凭证获取（GET）
    public final static String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 用户同意授权,获取code
    public final static String WX_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    // 用户同意授权,回调url
    public final static String WX_REDIRECT_URL = "回调url";
    // 拉取用户信息url
    public final static String WX_SNSAPI_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //微信openId
    public final static String WX_OPEN_ID = "oDLUf5cFGMF4FjEUMjBJessKXitI";
    //微信appid
    public final static String WX_APP_ID = "wxdee53430e84ef6ad";
    //微信appSecret
    public final static String WX_APP_SECRET = "e49c4c59187af413c5cf20dcf0949008";


    //redis过期时间
    public final static Expiration expire = Expiration.seconds(7200);//7200秒后数据过期

    private int code;
    private String msg;
    //通用的错误码
    public static Constant SUCCESS = new Constant(200, "success");
    public static Constant SERVER_ERROR = new Constant(500100, "服务端异常");
    public static Constant BIND_ERROR = new Constant(500101, "参数校验异常：%s");
    public static Constant Redis_TIMEDOWN = new Constant(500102, "Redis连接超时");
    public static Constant ROLE_ERROR = new Constant(500103, "您没有该权限");
    public static Constant RoleShop_ERROR = new Constant(500104, "商家至少有一个店铺！");
    public static Constant RoleCompany_ERROR = new Constant(500105, "物流公司至少有一个地址！");
    //登录模块 5002XX
    public static Constant TOKEN_ERROR = new Constant(500210, "Token不存在或者已经失效，请先登录！");
    public static Constant PASSWORD_EMPTY = new Constant(500211, "登录密码不能为空");
    public static Constant MOBILE_EMPTY = new Constant(500212, "手机号不能为空");
    public static Constant MOBILE_ERROR = new Constant(500213, "手机号格式错误");
    public static Constant MOBILE_NOT_EXIST = new Constant(500214, "手机号不存在");
    public static Constant PASSWORD_ERROR = new Constant(500215, "密码错误");

    //收货地址模块 5003XX
    public static Constant Addr_BEYOND = new Constant(500301, "最多存放5个我的地址");
    public static Constant Addr_SAVESUCCESS = new Constant(500302, "保存地址成功");
    public static Constant Addr_UPDATESUCCESS = new Constant(500303, "更新地址成功");
    public static Constant Addr_DELETESUCCESS = new Constant(500304, "删除地址成功");
    public static Constant TEL_WRONG = new Constant(500305, "电话号码错误");

    //订单模块
    public static Constant BILL_CREATESUCCESS = new Constant(500401, "下单成功");
    public static Constant BILL_CREATEFAILURE = new Constant(500402, "下单失败");
    public static Constant BILL_SEARCHFAILURE = new Constant(500403, "未找到相关订单");
    public static Constant BILL_UPDATEFAILURE = new Constant(500404, "更新失败");
    public static Constant BILL_UPDATESUCCESS = new Constant(500405, "更新成功");
    public static Constant BILL_DELETEFAILURE = new Constant(500406, "删除失败");
    public static Constant BILL_DELETESUCCESS = new Constant(500407, "删除成功");
    public static Constant BILL_RECEIVEFAILURE = new Constant(500404, "您来晚了，订单被别人抢走了");
    public static Constant BILL_RECEIVESUCCESS = new Constant(500405, "抢单成功");

    //订单状态码
    public static Constant BILL_STATUSONE = new Constant(1, "已下单，待接单");
    public static Constant BILL_STATUSTWO = new Constant(2, "已接单，待揽收");
    public static Constant BILL_STATUSTHREE = new Constant(3, "已揽收，托运中");
    public static Constant BILL_STATUSFOUR = new Constant(4, "托运完成");

    //公司模块
    public static  Constant COMPANY_ADDFAILURE = new Constant(500501, "添加公司失败");
    public static  Constant COMPANY_GETFAILURE = new Constant(500502, "获取公司列表失败");
    public static  Constant COMPANY_UPDATEFAILURE = new Constant(500503, "修改公司失败");
    public static  Constant COMPANYLINE_ADDORUPDATEFAILURE = new Constant(500504, "添加或更新公司线路失败");
    public static  Constant COMPANYLINE_DELETEFAILURE = new Constant(500505, "删除公司线路失败");
    public static  Constant COMPANY_DELETEFAILURE = new Constant(500506, "删除公司失败");

    //商户模块
    public static  Constant SHOP_ADDFAILURE = new Constant(500601, "添加商铺失败");
    public static  Constant SHOP_GETFAILURE = new Constant(500602, "获取商铺列表失败");
    public static  Constant SHOP_UPDATEFAILURE =  new Constant(500603, "修改商铺失败");

    //个人信息模块
    public static  Constant PERSONINFO_FAILURE = new Constant(500701, "获取个人信息失败");

    private Constant( ) { }
    private Constant( int code,String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
