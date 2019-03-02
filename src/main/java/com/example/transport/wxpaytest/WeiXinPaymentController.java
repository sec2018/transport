package com.example.transport.wxpaytest;

import com.alibaba.fastjson.JSONObject;
import com.example.transport.api.Common;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.util.JSONUtil;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import com.github.wxpay.sdk.WXPayUtil;
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

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private String notify_url = "https://wzjshuye.cn:8882/transport/api/paycallback";//通知地址
    private final String trade_type = "JSAPI";//交易类型
    private final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单API接口链接
    private final String key = Constant.WX_SHOP_KEY; // 商户支付密钥
    private final String appid = Constant.WX_APP_ID;
    private static Map<String,Integer> notifymap = new HashMap<>();

    @Autowired
    private RedisService redisService;

    @Autowired
    private BillService billService;

    /**
     *
     * @param total_fee 订单总金额，单位为分。
     * @param body  商品简单描述，该字段请按照规范传递。
     * @param attach    附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
     * @return
     * @throws UnsupportedEncodingException
     * @throws DocumentException
     */
    //支付接口
    @ApiOperation(value = "支付接口", notes = "支付接口")
    @RequestMapping(value="payment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单编号", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "total_fee", value = "费用总计", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "body", value = "简单描述", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "attach", value = "附加数据", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    public ResponseEntity<JsonResult> payment(@RequestParam(required = true)Integer order_id,@RequestParam(required = true)String total_fee, @RequestParam(required = false) String body, @RequestParam(required = false) String attach,HttpServletRequest request) throws UnsupportedEncodingException, DocumentException {

        total_fee = 1+"";
        System.out.println(notify_url);
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

                notifymap.put(out_trade_no,order_id);

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

                System.out.println("请求微信预支付接口，返回 code：" + return_code);
                System.out.println("请求微信预支付接口，返回 msg：" + return_msg);

                Map<String, String> res = new HashMap<String, String>();//返回给小程序端需要的参数
                String prepay_id = null;
                if(return_code=="SUCCESS"||return_code.equals(return_code)){
                    prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                    res.put("nonceStr", paymentPo.getNonce_str());
                    res.put("package", "prepay_id=" + prepay_id);
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    res.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                    //拼接签名需要的参数
                    String stringSignTemp = "appId=" + appid + "&nonceStr=" + paymentPo.getNonce_str() + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                    //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                    String paySign = com.example.transport.sdk.PayUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();

                    res.put("paySign", paySign);
                }
                System.out.println(res);
                r.setCode("200");
                r.setMsg("预支付订单成功！");
                r.setData(res);
                r.setSuccess(true);
                return ResponseEntity.ok(r);


            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付回调
     * @param request
     * @param response
     * @throws InterruptedException
     */
//    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value="paycallback")
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);

        Map map = com.example.transport.sdk.PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        String out_trade_no = (String) map.get("out_trade_no");
        String transaction_id = (String) map.get("transaction_id");
        System.out.println(out_trade_no);
        int billid = notifymap.get(out_trade_no);
        System.out.println(billid);
        if("SUCCESS".equals(returnCode)){

            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!WXPayUtil.isSignatureValid(sb.toString(), key)) {
                System.out.println("验证签名结果[失败].");
                //验签失败，需解决验签问题
            }else {
                //验证签名是否正确
                Map<String, String> validParams = com.example.transport.sdk.PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
                String validStr = com.example.transport.sdk.PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
                String sign = com.example.transport.sdk.PayUtil.sign(validStr, key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
                //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
                // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

                //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
                if(sign.equals(map.get("sign"))){
                    /**此处添加自己的业务逻辑代码start**/
                    // bla bla bla....
                    Date datetime = new Date();

                    System.out.println("我自己的业务逻辑！billid="+billid);
                    //已经ok
                    boolean flag =  billService.payNotifyBill(new Date(),billid,out_trade_no,transaction_id);
                    if(flag) {
                        String unbillstr = redisService.get("unbilllist");
                        List<SysBill> unbilllist = JSONUtil.jsonToList(unbillstr,SysBill.class);
                        for(SysBill sbill : unbilllist){
                            if (sbill.getId() == billid) {
                                sbill.setBill_status(3);
                            }
                        }
                        String unbillliststr = JSONUtil.listToJson(unbilllist);
                        redisService.set("unbilllist", unbillliststr);
                    }

                    /**此处添加自己的业务逻辑代码end**/
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                } else {
                    System.out.println("微信支付回调失败!签名不一致");
                }

            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
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