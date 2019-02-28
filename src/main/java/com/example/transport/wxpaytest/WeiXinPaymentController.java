package com.example.transport.wxpaytest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.transport.service.Constant;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api")
@Controller
public class WeiXinPaymentController{
    private static final long serialVersionUID = 1L;
    private final String mch_id = Constant.WX_SHOP_ID;//商户号
    private final String spbill_create_ip = "192.168.100.100";//终端IP
    private final String notify_url = "10.84.5.236:8882/transport/api/paycallback";//通知地址
    private final String trade_type = "JSAPI";//交易类型
    private final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单API接口链接
    private final String key = Constant.WX_SHOP_KEY; // 商户支付密钥
    private final String appid = Constant.WX_APP_ID;

    @Autowired
    private RedisService redisService;

    /**
     *
     * @param total_fee 订单总金额，单位为分。
     * @param body  商品简单描述，该字段请按照规范传递。 例：腾讯充值中心-心悦会员充值
     * @param attach    附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 例：广州分店
     * @return
     * @throws UnsupportedEncodingException
     * @throws DocumentException
     */
    //支付接口
    @ApiOperation(value = "支付接口", notes = "支付接口")
    @RequestMapping(value="payment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "total_fee", value = "费用总计", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "body", value = "简单描述", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "attach", value = "附加数据", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public JSONObject payment(@RequestParam(required = true)String total_fee, @RequestParam(required = false) String body, @RequestParam(required = false) String attach,HttpServletRequest request) throws UnsupportedEncodingException, DocumentException {

        total_fee = 1+"";

        String token = request.getHeader("token");
        JsonResult r = ConnectRedisCheckToken(token);
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

                JSONObject JsonObject = new JSONObject() ;
                body = new String(body.getBytes("UTF-8"),"ISO-8859-1");
                String nonce_str = UUIDHexGenerator.generate();//随机字符串
                String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String code = PayUtil.createCode(8);
                String out_trade_no = mch_id + today + code;//商户订单号

//        String openid = openId;//用户标识
                PaymentDto paymentPo = new PaymentDto();
                paymentPo.setAppid(appid);
                paymentPo.setMch_id(mch_id);
                paymentPo.setNonce_str(nonce_str);
                String newbody = new String(body.getBytes("ISO-8859-1"),"UTF-8");//以utf-8编码放入paymentPo，微信支付要求字符编码统一采用UTF-8字符编码
                paymentPo.setBody(newbody);
                paymentPo.setOut_trade_no(out_trade_no);
                paymentPo.setTotal_fee(total_fee);
//                paymentPo.setSpbill_create_ip(spbill_create_ip);
                paymentPo.setNotify_url(notify_url);
                paymentPo.setTrade_type(trade_type);
                paymentPo.setOpenid(openid);


                // 把请求参数打包成数组
//                Map<String, Object> sParaTemp = new HashMap();
//                sParaTemp.put("appid", paymentPo.getAppid());
//                sParaTemp.put("mch_id", paymentPo.getMch_id());
//                sParaTemp.put("nonce_str", paymentPo.getNonce_str());
//                sParaTemp.put("body",  paymentPo.getBody());
//                sParaTemp.put("out_trade_no", paymentPo.getOut_trade_no());
//                sParaTemp.put("total_fee",paymentPo.getTotal_fee());
////                sParaTemp.put("spbill_create_ip", paymentPo.getSpbill_create_ip());
//                sParaTemp.put("notify_url",paymentPo.getNotify_url());
//                sParaTemp.put("trade_type", paymentPo.getTrade_type());
//                sParaTemp.put("openid", paymentPo.getOpenid());
//                // 除去数组中的空值和签名参数
//                Map sPara = PayUtil.paraFilter(sParaTemp);
//                String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
//
//                //MD5运算生成签名
//                String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();
//                paymentPo.setSign(mysign);


//                //打包要发送的xml
//                String respXml = XmlUtil.messageToXML(paymentPo);
//                // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
//                respXml = respXml.replace("__", "_");
//                System.out.println(respXml);
//                String param = respXml;


                Map<String, String> packageParams = new HashMap<String, String>();
                packageParams.put("appid", appid);
                packageParams.put("mch_id", mch_id);
                packageParams.put("nonce_str", paymentPo.getNonce_str());
                packageParams.put("body", paymentPo.getBody());
                packageParams.put("out_trade_no", paymentPo.getOut_trade_no());//商户订单号
                packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
                packageParams.put("notify_url", paymentPo.getNotify_url());//支付成功后的回调地址
                packageParams.put("trade_type", trade_type);//支付方式
                packageParams.put("openid", paymentPo.getOpenid());

                String prestr = com.example.transport.sdk.PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

                //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
                String mysign = com.example.transport.sdk.PayUtil.sign(prestr, key, "utf-8").toUpperCase();


                //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
                String xml = "<xml>" + "<appid>" + appid + "</appid>"
                        + "<body><![CDATA[" + paymentPo.getBody() + "]]></body>"
                        + "<mch_id>" + mch_id + "</mch_id>"
                        + "<nonce_str>" + paymentPo.getNonce_str() + "</nonce_str>"
                        + "<notify_url>" + paymentPo.getNotify_url() + "</notify_url>"
                        + "<openid>" + paymentPo.getOpenid() + "</openid>"
                        + "<out_trade_no>" + paymentPo.getOut_trade_no() + "</out_trade_no>"
		       /* + "<spbill_create_ip>" + paymentPo.getSpbill_create_ip() + "</spbill_create_ip>" */
                        + "<total_fee>" + total_fee + "</total_fee>"
                        + "<trade_type>" + trade_type + "</trade_type>"
                        + "<sign>" + mysign + "</sign>"
                        + "</xml>";



                //String result = SendRequestForUrl.sendRequest(url, param);//发起请求
                String result = PayUtil.httpRequest(url, "POST", xml);
                System.out.println("请求微信预支付接口，返回 result："+result);
                // 将解析结果存储在Map中
                Map map = new HashMap();
                InputStream in=new ByteArrayInputStream(result.getBytes());
                // 读取输入流
                SAXReader reader = new SAXReader();
                Document document = reader.read(in);
                // 得到xml根元素
                Element root = document.getRootElement();
                // 得到根元素的所有子节点
                List<Element> elementList = root.elements();
                for (Element element : elementList) {
                    map.put(element.getName(), element.getText());
                }
                // 返回信息
                String return_code = map.get("return_code").toString();//返回状态码
                String return_msg = map.get("return_msg").toString();//返回信息
                String result_code = map.get("result_code").toString();//返回状态码

                System.out.println("请求微信预支付接口，返回 code：" + return_code);
                System.out.println("请求微信预支付接口，返回 msg：" + return_msg);
                if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)){
                    // 业务结果
                    String prepay_id = map.get("prepay_id").toString();//返回的预付单信息
                    String nonceStr = UUIDHexGenerator.generate();
                    JsonObject.put("nonceStr", nonceStr);
                    JsonObject.put("package", "prepay_id=" + prepay_id);
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    JsonObject.put("timeStamp", timeStamp + "");
                    String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                    //再次签名
                    String paySign = PayUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();
                    JsonObject.put("paySign", paySign);
                }
                return JsonObject;

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 预支付时填写的 notify_url ，支付成功后的回调接口
     * @param request
     */
    @ApiOperation(value = "支付回调接口", notes = "支付回调接口")
    @RequestMapping(value="paycallback")
    @ResponseBody
    public void paycallback(HttpServletRequest request) {
        try {
            Map<String, Object> dataMap = XmlUtil.parseXML(request);
            System.out.println(JSON.toJSONString(dataMap));
            //{"transaction_id":"4200000109201805293331420304","nonce_str":"402880e963a9764b0163a979a16e0002","bank_type":"CFT","openid":"oXI6G5Jc4D44y2wixgxE3OPwpDVg","sign":"262978D36A3093ACBE4B55707D6EA7B2","fee_type":"CNY","mch_id":"1491307962","cash_fee":"10","out_trade_no":"14913079622018052909183048768217","appid":"wxa177427bc0e60aab","total_fee":"10","trade_type":"JSAPI","result_code":"SUCCESS","time_end":"20180529091834","is_subscribe":"N","return_code":"SUCCESS"}
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}