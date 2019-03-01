package com.example.transport.wxrefundtest;

import com.alibaba.fastjson.JSONObject;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.util.AESUtil;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("api")
@Controller
public class WXRefundController {
    private static final long serialVersionUID = 1L;
    private final String mch_id = Constant.WX_SHOP_ID;//商户号
    private final String spbill_create_ip = "192.168.100.100";//终端IP
    private String notify_url = "https://wzjshuye.cn:8882/transport/api/refundcallback";//通知地址
    private final String trade_type = "JSAPI";//交易类型
    private final String refundurl = "https://api.mch.weixin.qq.com/secapi/pay/refund";//统一退款API接口链接
    private final String key = Constant.WX_SHOP_KEY; // 商户支付密钥
    private final String appid = Constant.WX_APP_ID;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BillService billService;

    @Transactional
    //退款接口
    @ApiOperation(value = "退款接口", notes = "退款接口")
    @RequestMapping(value="refund")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单编号", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "total_fee", value = "费用总计", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "out_trade_no", value = "商户订单号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "attach", value = "附加数据", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> refundOrder(@RequestParam(required = true)Integer order_id, @RequestParam(required = true)String total_fee, @RequestParam(required = true) String out_trade_no, @RequestParam(required = false) String attach, HttpServletRequest request) throws UnsupportedEncodingException, DocumentException{
        String token = request.getHeader("token");
        JsonResult r = ConnectRedisCheckToken(token);
        total_fee =  1+"";
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(tokenvalue != ""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];

                //创建hashmap(用户获得签名)
                SortedMap<String, String> packageParams = new TreeMap<String, String>();

                packageParams.put("appid", appid);
                packageParams.put("mch_id", mch_id);

                String nonceStr = UUID.randomUUID().toString().replace("-", "");
                packageParams.put("nonce_str",nonceStr);

                //设置商户退款单号
                Integer randomNumber = new Random().nextInt(900)+ 100;
                String out_refund_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+randomNumber;
                packageParams.put("out_refund_no",out_refund_no);

                packageParams.put("out_trade_no",out_trade_no);

                packageParams.put("total_fee", total_fee);
                //设置请求参数(退款金额)
                packageParams.put("refund_fee", total_fee);
                //TODO (这个回调地址 没有具体进行测试 需要写好逻辑 打版在测试)设置请求参数(通知地址)
                packageParams.put("notify_url", notify_url);

                String stringA = formatUrlMap(packageParams, false, false);
                //第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
                String sign = MD5Util.MD5(stringA+"&key="+key).toUpperCase();
                //将参数 编写XML格式
                StringBuffer paramBuffer = new StringBuffer();
                paramBuffer.append("<xml>");
                paramBuffer.append("<appid>"+appid+"</appid>");
                paramBuffer.append("<mch_id>"+ mch_id+"</mch_id>");
                paramBuffer.append("<nonce_str>"+packageParams.get("nonce_str")+"</nonce_str>");
                paramBuffer.append("<sign>"+sign+"</sign>");
                paramBuffer.append("<out_refund_no>"+packageParams.get("out_refund_no")+"</out_refund_no>");
                paramBuffer.append("<out_trade_no>"+packageParams.get("out_trade_no")+"</out_trade_no>");
                paramBuffer.append("<refund_fee>"+packageParams.get("refund_fee")+"</refund_fee>");
                paramBuffer.append("<total_fee>"+packageParams.get("total_fee")+"</total_fee>");
                paramBuffer.append("<notify_url>"+packageParams.get("notify_url")+"</notify_url>");
                paramBuffer.append("</xml>");
                //设置最终返回对象
                JSONObject resultJson = new JSONObject();
                try {
                    //发送请求(POST)(获得数据包ID)(这有个注意的地方 如果不转码成ISO8859-1则会告诉你body不是UTF8编码 就算你改成UTF8编码也一样不好使 所以修改成ISO8859-1)
//                    Map<String,String> map = doXMLParse(doRefund(request,refundurl, new String(paramBuffer.toString().getBytes(), "ISO8859-1")));
                    Map<String,String> map = WXPayUtil.doXMLParse(doRefund(request,refundurl, new String(paramBuffer.toString().getBytes(), "ISO8859-1")));
                    //应该创建 退款表数据
                    if(map!=null && (StringUtils.isNotBlank(map.get("return_code")) && "SUCCESS".equals(map.get("return_code")))){
                        if(StringUtils.isBlank(map.get("err_code_des"))){
                            //执行成功
                            r.setCode("200");
                            r.setMsg("预退款订单成功！");
                            resultJson.put("returnCode", "success");
                            r.setSuccess(true);
                        }else{
                            resultJson.put("returnCode", "error");
                            resultJson.put("err_code_des", map.get("err_code_des"));
                            r.setCode("500");
                            r.setMsg("预退款订单异常！");
                            r.setSuccess(false);
                        }
                    }else {
                        resultJson.put("returnCode", map.get("return_code"));
                        resultJson.put("err_code_des", map.get("err_code_des"));
                        r.setCode("500");
                        r.setMsg("预退款订单异常！");
                        r.setSuccess(false);
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("微信 退款 异常："+e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("微信 退款 异常："+e.getMessage());
                    e.printStackTrace();
                }
                r.setData(resultJson);
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public JsonResult ConnectRedisCheckToken(String token){
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry<=3){
            try
            {
                //业务代码
                tokenvalue = redisService.get(token);
                r.setCode(200+"");
                r.setData(tokenvalue);
                r.setMsg("连接成功！");
                r.setSuccess(true);
                break;
            }
            catch(Exception ex)
            {
                //重试
                retry++;
                if(retry == 4){
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
                    r.setData("");
                    r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
                    r.setSuccess(false);
                    return r;
                }
            }
        }
        return r;
    }


    private String doRefund(HttpServletRequest request,String url,String data) throws Exception{
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream("D:\\WXResource\\apiclient_cert.p12");//P12文件目录 证书路径
        try {
            /**
             * 此处要改
             * */
            keyStore.load(instream, mch_id.toCharArray());//这里写密码.
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        /**
         * 此处要改
         * */
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mch_id.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }


    /**
     *  方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<
     * @param paramsMap 要排序的Map对象
     * @param urlEncode 是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写 true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paramsMap, boolean urlEncode, boolean keyToLower) {

        String buff = "";
        Map<String, String> tmpMap = paramsMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());

            //对所有传入参数按照字段名的ASCII码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            //构造URL 键值对的格式
            StringBuffer buf = new StringBuffer();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String value = item.getValue();
                    if (urlEncode) {
                        value = URLEncoder.encode(value, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + value);
                    } else {
                        buf.append(key + "=" + value);
                    }
                    buf.append("&");
                }
            }
            buff = buf.toString();

            if (StringUtils.isNotEmpty(buff)) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return buff;
    }

    @RequestMapping(value="refundcallback")
    public void refundCallback(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("退款  微信回调接口方法 start");
        String inputLine = "";
        String notityXml = "";
        try {
            while((inputLine = request.getReader().readLine()) != null){
                notityXml += inputLine;
            }
            //关闭流
            request.getReader().close();
            System.out.println("退款  微信回调内容信息："+notityXml);
            //解析成Map
            Map<String,String> map = WXPayUtil.doXMLParse(notityXml);
            //判断 退款是否成功
            if("SUCCESS".equals(map.get("return_code"))){
                System.out.println("退款  微信回调返回是否退款成功：是");
                System.out.println(map.get("req_info"));
                //获得 返回的商户订单号
                String passMap = WXAESUtil.decryptData(map.get("req_info"));
                //拿到解密信息
                map = WXPayUtil.doXMLParse(passMap);
                //拿到解密后的订单号
                String outTradeNo = map.get("out_trade_no");
                String refundcode = map.get("out_refund_no");
                System.out.println("退款  微信回调返回商户订单号："+outTradeNo);
                //支付成功 修改订单状态 通知微信成功回调
                System.out.println("我自己的逻辑");
                //已经ok
                boolean flag =  billService.refundBill(new Date(),outTradeNo,1,refundcode);
                if(flag) {
                    System.out.println("退款 微信回调 更改订单状态成功");
                }
            }else {
                //获得 返回的商户订单号
                String passMap = WXAESUtil.decryptData(map.get("req_info"));
                //拿到解密信息
                map = WXPayUtil.doXMLParse(passMap);
                //拿到解密后的订单号
                String outTradeNo = map.get("out_trade_no");
                String refundcode = map.get("out_refund_no");
                //更改 状态为取消
                System.out.println("我自己的逻辑  退款 微信回调返回是否退款成功：否");
                boolean flag =  billService.refundBill(new Date(),outTradeNo,0,refundcode);
                if(flag) {
                    System.out.println("退款 微信回调 已更改订单退款状态为0");
                }
            }
            //给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
            PrintWriter writer = response.getWriter();
            //封装 返回值
            StringBuffer buffer = new StringBuffer();
            buffer.append("<xml>");
            buffer.append("<return_code><![CDATA[SUCCESS]]></return_code>");
            buffer.append("<return_msg><![CDATA[OK]]></return_msg>");
            buffer.append("</xml>");
            //返回
            writer.print(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @PostConstruct
//    public void init() throws Exception{
//        String notityXml = "<xml><return_code>SUCCESS</return_code><appid><![CDATA[wx2e17c4169a946806]]></appid><mch_id><![CDATA[1526798551]]></mch_id><nonce_str><![CDATA[23d36631938f2fbf0c2c46dbe61b9112]]></nonce_str><req_info><![CDATA[cEnoB1lwgUpNwgyeZWbKJIrErwO4z/adkzk1P+eyGAlSENts8mQbRNnmd3ZlyNX8Naf3RVpturpNw7njIyHaZ6U2/ZlbgNnR9MRhbo9aXwr5/SipilGzJK5SS425f0bomW5ZdVE4HD2GyvBvG6wLWVxqax1QPDQAGpCr+/fdddHVdI76RDo4Zm/XFZpOBqPwy4G7O32krGm9hlUHeGIWaRwzTMvWacd7T6ARub2ywMIGRH7ohfn7ZkjeKOh7pSxvgdvuBFt9dyh1sV8Vr64RafJIUWSObaqKTuU4LlslzicHLt3jScifoFqw+9qMFLRS+dmbzXeLJp1opv8CmPNzL7SCGdbm34qD2wYayRbo+JE9qS0UsXRUws53EdIyvcmYuTUUGmjd4rpaaWbJhtW53SJGWz7gUTtMZTVPZONdCM6giYtJebZ8k+65NasScMxrczxkv1yclHyC7fPz0ULUaKL/fC9sydN5y4UzT/AJGuAm6NJ9t1mxJ5V0ijPBaXhhFS69F4OEgPYV80uiaVX4GbgfZb2O59v3nrv7uLpRE4lZK0HjMxMf6+lS4Htjf83CoD657HdS3THaA9RK3zAf3/6OsVSBYsmwp/4QL5vBhpqokLuDoc7Mn3W1E+HO9gHW9c+l71fsWs+jxLzPvINzLPqjMfXs5KT1tGtBt6roUXtjY5iiwmjgzd5TXFJEWDI4+/rGjvLDxyIOJiMN5l/uFlSH4jk0x3nWDc0bDZOP/y7JZWK6uD6XU0LjMlGZJ3VmI/yyxZf71sy1LpSutQ/ysA4Vm78XGw6sbwdbtlJm7VDS0u1uAkgy/tEyRaL+JVuLYMZx03HOGftgpbSlI/z49tlTEdUYi6Gx1TB9ORDZlVhRPCSDESug734IeRuvG6ES1EmEdMZHj2jTpopTMAuhVTnpPOOLDaomRn2xh43O3XqHgPi3wgI5yoZ1XJYp2b2nq+wj/0yqxT2J8OZ6S4RXpcXGMgY6yKU1WThubSlZnXOvoIUBgmSq8J+GQnEFMDPjT1aD9pvmNmrcgKkpTwuX7+z33pw3Nv1ZbKdF7OlGmplOEVbFEuE13FzyLKHURgxU]]></req_info></xml>";
//        Map<String,String> map = WXPayUtil.doXMLParse(notityXml);
//        //判断 退款是否成功
//        if("SUCCESS".equals(map.get("return_code"))){
//            System.out.println("退款  微信回调返回是否退款成功：是");
//            System.out.println(map.get("req_info"));
//            //获得 返回的商户订单号
//            String passMap = WXAESUtil.decryptData(map.get("req_info"));
//            //拿到解密信息
//            System.out.println(passMap);
//            map = WXPayUtil.doXMLParse(passMap);
//            String value = "";
//            for (String key : map.keySet()) {
//                value = map.get(key)+"  ";
//            }
//            System.out.println(value);
//            //拿到解密后的订单号
//            String outTradeNo = map.get("out_trade_no");
//            String refundcode = map.get("out_refund_no");
//            System.out.println("退款  微信回调返回商户订单号："+outTradeNo);
//            //支付成功 修改订单状态 通知微信成功回调
//            System.out.println("我自己的逻辑");
//
////            已经ok
////            boolean flag =  billService.refundBill(new Date(),"15267985512019022819400368222820",1,refundcode);
////            if(flag) {
////                System.out.println("退款 微信回调 更改订单状态成功");
////            }
//        }
//    }
}
