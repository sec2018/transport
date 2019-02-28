package com.example.transport.sdk;

import com.example.transport.service.Constant;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("api")
@Controller
public class WXPayApi {

    private static final long serialVersionUID = 1L;
    private static final String appid = Constant.WX_APP_ID;
    private static final String mchId = Constant.WX_SHOP_ID;
    private static final String key = Constant.WX_SHOP_KEY;
    private static final String tradeType = "JSAPI";
    private static final String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    public String getOpenId(String code) throws Exception {

        try {
            //登录凭证校验
            String loginValidataUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+ Constant.WX_APP_ID+"&secret="+Constant.WX_APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";

            Connection conn = Jsoup.connect(loginValidataUrl);
            conn.method(Connection.Method.GET).execute();
            int status = conn.response().statusCode();//200成功
            if (status!=200) {
                //抛异常
                throw new Exception();
            }
            String body = conn.response().body();

            return body;
        } catch (Exception e) {
            throw new Exception();
        }
    }

//    @RequestMapping(value="payment")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "total_fee", value = "费用总计", required = true, dataType = "Integer",paramType = "query"),
//            @ApiImplicitParam(name = "body", value = "简单描述", required = true, dataType = "Integer",paramType = "query"),
//            @ApiImplicitParam(name = "attach", value = "附加数据", required = true, dataType = "Integer",paramType = "query"),
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header")
//    })
    public Map<String, String> goPay(@RequestParam(required = true)String total_fee, @RequestParam(required = false) String body, @RequestParam(required = false) String attach, HttpServletRequest request) throws Exception {
        //商品名称
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setNonce_str("4028b88169333d4a0169333d4a4d0000");
        paymentPo.setBody("头套");
        paymentPo.setOut_trade_no("15267985512019022816340861844307");
        paymentPo.setTotal_fee("1");
        paymentPo.setNotify_url("http://localhost:8882/transport/api/paynotify");
        paymentPo.setOpenid("oU27c4rjJaOZNhmSZpnz1DsV2xTc");



        //String body = "测试商品名称";
        //金额元=paymentPo.getTotal_fee()*100
        total_fee = String.valueOf(new BigDecimal(paymentPo.getTotal_fee()).multiply(new BigDecimal(100)).intValue());
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mchId);
        packageParams.put("nonce_str", paymentPo.getNonce_str());
        packageParams.put("body", paymentPo.getBody());
        packageParams.put("out_trade_no", paymentPo.getOut_trade_no());//商户订单号
        packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("notify_url", paymentPo.getNotify_url());//支付成功后的回调地址
        packageParams.put("trade_type", tradeType);//支付方式
        packageParams.put("openid", paymentPo.getOpenid());

        String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

        //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();

        //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
        String xml = "<xml>" + "<appid>" + appid + "</appid>"
                + "<body><![CDATA[" + paymentPo.getBody() + "]]></body>"
                + "<mch_id>" + mchId + "</mch_id>"
                + "<nonce_str>" + paymentPo.getNonce_str() + "</nonce_str>"
                + "<notify_url>" + paymentPo.getNotify_url() + "</notify_url>"
                + "<openid>" + paymentPo.getOpenid() + "</openid>"
                + "<out_trade_no>" + paymentPo.getOut_trade_no() + "</out_trade_no>"
		       /* + "<spbill_create_ip>" + paymentPo.getSpbill_create_ip() + "</spbill_create_ip>" */
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<trade_type>" + tradeType + "</trade_type>"
                + "<sign>" + mysign + "</sign>"
                + "</xml>";

        System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);

        //调用统一下单接口，并接受返回的结果
        String res = PayUtil.httpRequest(payUrl, "POST", xml);

        System.out.println("调试模式_统一下单接口 返回XML数据：" + res);

        // 将解析结果存储在HashMap中
        Map map = PayUtil.doXMLParse(res);

        String return_code = (String) map.get("return_code");//返回状态码

        Map<String, String> result = new HashMap<String, String>();//返回给小程序端需要的参数
        String prepay_id = null;
        if(return_code=="SUCCESS"||return_code.equals(return_code)){
            prepay_id = (String) map.get("prepay_id");//返回的预付单信息

            result.put("nonceStr", paymentPo.getNonce_str());
            result.put("package", "prepay_id=" + prepay_id);
            Long timeStamp = System.currentTimeMillis() / 1000;
            result.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //拼接签名需要的参数
            String stringSignTemp = "appId=" + appid + "&nonceStr=" + paymentPo.getNonce_str() + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = PayUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();

            result.put("paySign", paySign);
        }
        result.put("appid", appid);
        return result;
    }


    /**
     * 支付回调
     * @param request
     * @param response
     * @throws InterruptedException
     */
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    @RequestMapping(value="/paynotify")
//    public synchronized void notify(HttpServletRequest request,HttpServletResponse response) throws InterruptedException{
//
//        String orderId = null;
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//            while((line = br.readLine()) != null){
//                sb.append(line);
//            }
//            br.close();
//            //sb为微信返回的xml
//            String notityXml = sb.toString();
//            String resXml = "";
//            System.out.println("接收到的报文：" + notityXml);
//
//            Map map = PayUtil.doXMLParse(notityXml);
//
//            String returnCode = (String) map.get("return_code");
//            if("SUCCESS".equals(returnCode)){
//                //验证签名是否正确
//                Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
//                String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
//                String sign = PayUtil.sign(validStr, key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
//                //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
//                if(sign.equals(map.get("sign"))){
//                    /**此处添加自己的业务逻辑代码start**/
//                    //TODO
//
//                    /**此处添加自己的业务逻辑代码end**/
//                    //通知微信服务器已经支付成功
////                    resXml = this.getXml();
//                    System.out.println("已支付成功");
//                }
//
//            }else{
////                resXml = this.getFailXml();
//                System.out.println("支付失败");
//            }
//            System.out.println(resXml);
//            System.out.println("微信支付回调数据结束");
//
//
//            BufferedOutputStream out = new BufferedOutputStream(
//                    response.getOutputStream());
//            out.write(resXml.getBytes());
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            System.out.println(orderId+e.getMessage());
//        }
//    }


}
