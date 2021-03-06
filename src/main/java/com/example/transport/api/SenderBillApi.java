package com.example.transport.api;

import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.dao.SysCompanyMapper;
import com.example.transport.pojo.*;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.ImageUtil;
import com.example.transport.util.JSONUtil;
import com.example.transport.util.JsonResult;
import com.example.transport.util.graphicsutils;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

@RequestMapping("api")
@Controller
public class SenderBillApi {

    @Autowired
    private BillService billService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private CompanyLinesMapper companyLinesMapper;

    Semaphore semaphore = new Semaphore(1);


    private static ConcurrentMap<String, String> imgBases = new ConcurrentHashMap<>();

    private int pay_method;
    private int give_method;
    private double keepfee;
    private int waitnote;

    //角色1,2, company_code传空值
    @ApiOperation(value = "商家下单接口", notes = "下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender_name", value = "下单人名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsname", value = "商品名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsnum", value = "商品数量", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "商家电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_id", value = "商铺id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
//            @ApiImplicitParam(name = "company_id", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "company_name", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "batch_code", value = "批量下单编号(0代表非批量,1代表批量已保存,或string代表批量成功可查询)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lng", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "billinfo", value = "运单备注", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_procity", value = "寄件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_detailarea", value = "寄件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_name", value = "收件人姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_tel", value = "收件人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_procity", value = "收件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_detailarea", value = "收件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "price", value = "总价", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "line_id", value = "线路标示", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "company_code", value = "运单号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pay_method", value = "付款方式 1,现付 2,到付, 3,回付 4,月结", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "give_method", value = "交付方式 1,送货 2,自提", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "keepfee", value = "代收货款", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "waitnote", value = "等通知放货 1 是 2 否", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "createbill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> createBill(@RequestParam(value = "sender_name")String sender_name,@RequestParam(value = "goodsname")String goodsname, @RequestParam(value = "goodsnum")Integer goodsnum,@RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_id")int shop_id,@RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "batch_code")String batch_code,@RequestParam(value = "lat")String lat,@RequestParam(value = "lng")String lng,@RequestParam(value = "billinfo")String billinfo,
                        @RequestParam(value = "sender_procity")String sender_procity,@RequestParam(value = "sender_detailarea")String sender_detailarea,@RequestParam(value = "rec_name")String rec_name,
                        @RequestParam(value = "rec_tel")String rec_tel,@RequestParam(value = "rec_procity")String rec_procity,@RequestParam(value = "rec_detailarea")String rec_detailarea,@RequestParam(value = "price")double price,@RequestParam(value = "line_id")int line_id,
                        @RequestParam(value = "company_code")String company_code, @RequestParam(value = "pay_method")int pay_method, @RequestParam(value = "give_method")int give_method,
                        @RequestParam(value = "keepfee")double keepfee, @RequestParam(value = "waitnote")int waitnote,HttpServletRequest request) {
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue != ""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);

                //创建订单
                SysBill sysBill = new SysBill();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                String bill_code = df.format(new Date()) + UUID.randomUUID().toString().substring(0,5);
                sysBill.setBill_code(bill_code);
                sysBill.setSender_id(wxuserid);    //wxuserid即为商户的sender_id
                sysBill.setGoodsname(goodsname);
                sysBill.setGoodsnum(goodsnum);
                sysBill.setSender_name(sender_name);
                sysBill.setSender_tel(sender_tel);
                sysBill.setShop_id(shop_id);
                sysBill.setShop_name(shop_name);
                int company_id = companyLinesMapper.selectByPrimaryKey(line_id).getCompanyId();
                sysBill.setCompany_id(company_id);
                sysBill.setCompany_name(company_name);
                sysBill.setBatch_code(batch_code);
                sysBill.setBill_status(1);
                sysBill.setTrans_id(-1);
                sysBill.setSender_lat(lat);
                sysBill.setSender_lng(lng);
                sysBill.setBillinfo(billinfo);
                sysBill.setSender_procity(sender_procity);
                sysBill.setSender_detailarea(sender_detailarea);
                sysBill.setRec_name(rec_name);
                sysBill.setRec_tel(rec_tel);
                sysBill.setRec_procity(rec_procity);
                sysBill.setRec_detailarea(rec_detailarea);
                sysBill.setPrice(price);
                sysBill.setCompany_code(company_code);
                sysBill.setLine_id(line_id);

                sysBill.setPay_method(pay_method);
                sysBill.setGive_method(give_method);
                sysBill.setKeepfee(keepfee);
                sysBill.setWaitnote(waitnote);

//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");//注意格式化的表达式
//                sysBill.setCreate_time(format.parse(format.format(new Date())));
                sysBill.setCreate_time(new Date());
                boolean flag = billService.insertBill(sysBill);
                if (flag) {
                    String unbillstr = redisService.get("unbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<SysBill> unbilllist = billService.selectAllUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("unbilllist");
                            redisService.set("unbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<SysBill> unbilllist = JSONUtil.jsonToList(unbillstr,SysBill.class);
                    unbilllist.add(sysBill);
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("unbilllist");
                    redisService.set("unbilllist", unbillliststr);
                    r.setCode("200");
                    if(batch_code.equals("1")){
                        r.setMsg("保存成功！");
                    }else{
                        r.setMsg("下单成功！");
                    }
                    r.setData(batch_code);
                    r.setSuccess(true);
                }else {
                    r.setCode("500");
                    r.setMsg("下单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_CREATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_CREATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //角色1,2
    @ApiOperation(value = "商家查询所下订单", notes = "根据商家标识查询所下订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender_id", value = "商家id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getuserbill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillByUserId(@RequestParam(value = "sender_id") long sender_id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                List<SysBill> billList = billService.selectUserBill(sender_id);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //角色1,2
    @ApiOperation(value = "商家查询所下未完成批量订单", notes = "商家查询所下未完成批量订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getbatchbill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBatchBill(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);
                List<SysBill> billlist = billService.selectBatchBills(wxuserid);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billlist);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //角色1,2
    @ApiOperation(value = "用户查询所下未完成订单(分页)", notes = "用户查询所下未完成订单(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startitem", value = "startitem", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "pagesize", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getunfinishedpagebills", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBatchBill(@RequestParam(value = "startitem") int startitem,@RequestParam(value = "pagesize") int pagesize,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                redisService.expire(token, Constant.expire.getExpirationTime());
                if(roleid.equals("2")){
                    //商家查询所下未完成订单
                    Map<String, Object> map = billService.selectUnfinishBill(wxuserid,startitem,pagesize);
                    r.setData(map);
                }else if(roleid.equals("3")){
                    //承运员未完成订单
                    Map<String, Object> map = billService.selectunfinishedBillByTransId(wxuserid,startitem,pagesize);
                    r.setData(map);
                }else if(roleid.equals("4")){
                    //物流公司未完成订单
                    int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                    Map<String, Object> map  = billService.selectunfinishedBillByCompanyId(companyid,startitem,pagesize);
                    r.setData(map);
                }else if(roleid.equals("1")){
                    //超级管理员查询未完成订单

                }else{
                    r = Common.RoleError();
                    return ResponseEntity.ok(r);
                }
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,2,3,4
    @ApiOperation(value = "通过id查询订单", notes = "根据订单标识查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getbill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillById(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                SysBill bill = billService.selectSingleBill(id);


                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(bill);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1,2
    @ApiOperation(value = "商户修改未接订单接口", notes = "商户修改未接订单接口")          //让修改哪些内容？
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "sender_name", value = "下单人名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "下单人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_id", value = "商铺id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_id", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "company_name", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsname", value = "商品名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsnum", value = "商品数量", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "batch_code", value = "批量下单编号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lng", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "billinfo", value = "运单备注", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_procity", value = "寄件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_detailarea", value = "寄件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_name", value = "收件人姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_tel", value = "收件人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_procity", value = "收件人省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rec_detailarea", value = "收件人详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "price", value = "总价", required = true, dataType = "Double",paramType = "query"),
//            @ApiImplicitParam(name = "delivery_fee", value = "物流运费", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "line_id", value = "线路标示", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pay_method", value = "付款方式 1,现付 2,到付, 3,回付 4,月结", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "give_method", value = "交付方式 1,送货 2,自提", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "keepfee", value = "代收货款", required = true, dataType = "Double",paramType = "query"),
            @ApiImplicitParam(name = "waitnote", value = "等通知放货 1 是 2 否", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="senderupdatebill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> updateBill(@RequestParam(value = "id") long id,@RequestParam(value = "sender_name")String sender_name, @RequestParam(value = "sender_tel")String sender_tel,@RequestParam(value = "shop_id")int shop_id,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,@RequestParam(value = "goodsname")String goodsname,
                        @RequestParam(value = "goodsnum")int goodsnum, @RequestParam(value = "batch_code")String batch_code,
                        @RequestParam(value = "lat")String lat,@RequestParam(value = "lng")String lng,
                        @RequestParam(value = "billinfo")String billinfo,@RequestParam(value = "sender_procity")String sender_procity,
                        @RequestParam(value = "sender_detailarea")String sender_detailarea,@RequestParam(value = "rec_name")String rec_name,
                        @RequestParam(value = "rec_tel")String rec_tel,@RequestParam(value = "rec_procity")String rec_procity,
                        @RequestParam(value = "rec_detailarea")String rec_detailarea,@RequestParam(value = "price")double price,
                        @RequestParam(value = "line_id")int line_id,
                        @RequestParam(value = "pay_method")int pay_method, @RequestParam(value = "give_method")int give_method,
                        @RequestParam(value = "keepfee")double keepfee, @RequestParam(value = "waitnote")int waitnote,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                SysBill sysBill = billService.selectSingleBill(id);

                sysBill.setSender_name(sender_name);
                sysBill.setSender_tel(sender_tel);
                sysBill.setShop_id(shop_id);
                sysBill.setShop_name(shop_name);
                sysBill.setCompany_id(company_id);
                sysBill.setCompany_name(company_name);
                sysBill.setGoodsname(goodsname);
                sysBill.setGoodsnum(goodsnum);
                sysBill.setBatch_code(batch_code);
                sysBill.setSender_lat(lat);
                sysBill.setSender_lng(lng);
                sysBill.setBillinfo(billinfo);
                sysBill.setSender_procity(sender_procity);
                sysBill.setSender_detailarea(sender_detailarea);
                sysBill.setRec_name(rec_name);
                sysBill.setRec_tel(rec_tel);
                sysBill.setRec_procity(rec_procity);
                sysBill.setRec_detailarea(rec_detailarea);
                sysBill.setPrice(price);
//                sysBill.setDelivery_fee(delivery_fee);
                sysBill.setLine_id(line_id);

                sysBill.setPay_method(pay_method);
                sysBill.setGive_method(give_method);
                sysBill.setKeepfee(keepfee);
                sysBill.setWaitnote(waitnote);

                boolean flag = billService.SenderUpdateBill(sysBill);
                if (flag) {
                    String unbillstr = redisService.get("unbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<SysBill> unbilllist = billService.selectAllUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("unbilllist");
                            redisService.set("unbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<SysBill> unbilllist = JSONUtil.jsonToList(unbillstr,SysBill.class);
                    unbilllist.add(sysBill);
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("unbilllist");
                    redisService.set("unbilllist", unbillliststr);
                    r = Common.BillUpdateSuccess();
                }else{
                    r = Common.BillUpdateFailure();
                }
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.BillUpdateError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1
    @ApiOperation(value = "删除未接订单或批量下单的一个订单接口", notes = "根据id删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="deletesenderbill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> deleteSenderbill(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                boolean flag = billService.deleteSenderUnRecBill(id,wxuserid);
                if (flag) {
                    List<SysBill> unbilllist = billService.selectAllUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("unbilllist");
                        redisService.set("unbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r = Common.DeleteSuccess();
                }else{
                    r = Common.DeleteFailure();
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_DELETEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_DELETEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1，2,3,4
    @ApiOperation(value = "用户现有订单接口(tab查询统一接口，分页)", notes = "用户现有订单接口(用tab查询统一接口，分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startitem", value = "startitem", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "pagesize", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "isfinishflag", value = "是否为完成的订单标志（0：未完成，1：完成）", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getusertabbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchunfinishBillByUserId(@RequestParam(value = "startitem") int startitem,@RequestParam(value = "pagesize") int pagesize,@RequestParam(value = "isfinishflag") int isfinishflag,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            //超级管理员,PC端
            if(roleid.equals("0")){
                tokenvalue = getAdminToken();
                if(!token.equals(tokenvalue)){
                    r = Common.TokenError();
                    return ResponseEntity.ok(r);
                }else{
                    if(isfinishflag == 0){
                        //超级管理员查询未完成订单
                        Map<String, Object> map = billService.adminSelectunfinishedBill(startitem,pagesize);
                        r.setData(map);
                    }else if(isfinishflag == 1){
                        //超级管理员查询已完成订单
                        Map<String, Object> map = billService.adminSelectfinishedBill(startitem,pagesize);
                        r.setData(map);
                    }
                }
            //其他角色
            }else{
                tokenvalue = r.getData().toString();
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        String openid = tokenvalue.split("\\|")[0];
                        long wxuserid = userService.getWxUserId(openid);
                        try {
                            if(tokenvalue!=""){
                                redisService.expire(token, Constant.expire.getExpirationTime());
                                if(isfinishflag == 0){
                                    if(roleid.equals("2")){
                                        //商家查询所下未完成订单
                                        Map<String, Object> map = billService.selectUnfinishBill(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("3")){
                                        //承运员未完成订单
                                        Map<String, Object> map = billService.selectunfinishedBillByTransId(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("4")){
                                        //物流公司未完成订单
                                        int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                                        Map<String, Object> map = billService.selectunfinishedBillByCompanyId(companyid,startitem,pagesize);
                                        r.setData(map);
                                    }else{
                                        r = Common.RoleError();
                                        return ResponseEntity.ok(r);
                                    }
                                }else if(isfinishflag == 1){
                                    if(roleid.equals("2")){
                                        //商家查询所下已完成订单
                                        Map<String, Object> map = billService.selectfinishedBill(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("3")){
                                        //承运员查询所下已完成订单
                                        Map<String, Object> map = billService.selectfinishedBillByTransId(wxuserid,startitem,pagesize);
                                        r.setData(map);
                                    }else if(roleid.equals("4")){
                                        //物流公司查询所下已完成订单
                                        int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                                        Map<String, Object> map = billService.selectfinishedBillByCompanyId(companyid,startitem,pagesize);
                                        r.setData(map);
                                    }else{
                                        r = Common.RoleError();
                                        return ResponseEntity.ok(r);
                                    }
                                }
                                r.setCode("200");
                                r.setMsg("查询成功！");
                                r.setSuccess(true);
                            }else{
                                r = Common.TokenError();
                            }
                        } catch (Exception e) {
                            r = Common.SearchError(e);
                            e.printStackTrace();
                        }
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.TokenError(e);
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            r = Common.TokenError();
            System.out.println(e.getMessage());
            return ResponseEntity.ok(r);
        }
        return ResponseEntity.ok(r);
    }


    //1，2,3,4
    @ApiOperation(value = "用户查询未完成订单(用tab查询统一接口替代)", notes = "用户查询未完成订单(用tab查询统一接口替代)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getuserunfinishbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchunfinishBillByUserId(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        if(roleid.equals("2")){
                            //商家查询所下未完成订单
                            List<SysBillAndLine> billList = billService.selectUnfinishBill(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("3")){
                            //承运员未完成订单
                            List<SysBill> billList = billService.selectunfinishedBillByTransId(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("4")){
                            //物流公司未完成订单
                            int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                            List<SysBill> billList = billService.selectunfinishedBillByCompanyId(companyid);
                            r.setData(billList);
                        }else if(roleid.equals("1")){
                            //超级管理员查询未完成订单

                        }else{
                            r = Common.RoleError();
                            return ResponseEntity.ok(r);
                        }
                        r.setCode("200");
                        r.setMsg("查询成功！");
                        r.setSuccess(true);
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.SearchError(e);
                    e.printStackTrace();
                }
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.TokenError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,2，3,4
    @ApiOperation(value = "查询所下已完成订单(用tab查询统一接口替代)", notes = "查询所下已完成订单(用tab查询统一接口替代)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getuserfinishedbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchFinishedsBillByUserId(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                try {
                    if(tokenvalue!=""){
                        redisService.expire(token, Constant.expire.getExpirationTime());
                        if(roleid.equals("2")){
                            //商家查询所下已完成订单
                            List<SysBill> billList = billService.selectfinishedBill(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("3")){
                            //承运员未完成订单
                            List<SysBill> billList = billService.selectfinishedBillByTransId(wxuserid);
                            r.setData(billList);
                        }else if(roleid.equals("4")){
                            //物流公司未完成订单
                            int companyid = sysCompanyMapper.selectCompanyIdbyWxuserid(wxuserid);
                            List<SysBill> billList = billService.selectfinishedBillByCompanyId(companyid);
                            r.setData(billList);
                        }else if(roleid.equals("1")){
                            //超级管理员查询未完成订单

                        }else{
                            r = Common.RoleError();
                            return ResponseEntity.ok(r);
                        }
                        r.setCode("200");
                        r.setMsg("查询成功！");
                        r.setSuccess(true);
                    }else{
                        r = Common.TokenError();
                    }
                } catch (Exception e) {
                    r = Common.SearchError(e);
                    e.printStackTrace();
                }
            }else{
                r = Common.TokenError();
            }
        } catch (Exception e) {
            r = Common.TokenError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1,3
    @ApiOperation(value = "查询所有所下未接运单", notes = "查询所有所下未接运单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getallunbills",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchAllUnbills(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3") && !roleid.equals("0")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        if(roleid.equals("0")){
            try {
                User user = userService.getUserByLoginName("system");
                if(token.equals(sysUserTokenService.getToken(user.getId()))){
                    List<SysBill> billList = billService.selectAllUnBills();
                    r.setCode("200");
                    r.setMsg("查询成功！");
                    r.setData(billList);
                    r.setSuccess(true);
                }else{
                    r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
                    r.setData(null);
                    r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
                    r.setSuccess(false);
                }
            } catch (Exception e) {
                r = Common.SearchError(e);
                e.printStackTrace();
            }
            return ResponseEntity.ok(r);
        }else{
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try{
                tokenvalue = r.getData().toString();
            }catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
            try {
                if(tokenvalue!=""){
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    List<SysBill> billList = billService.selectAllUnBills();
                    r.setCode("200");
                    r.setMsg("查询成功！");
                    r.setData(billList);
                    r.setSuccess(true);
                }else{
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r = Common.SearchError(e);
                e.printStackTrace();
            }
            return ResponseEntity.ok(r);
        }
    }

    //查询所有最近2公里未接订单
    @ApiOperation(value = "查询所有最近2公里未接订单", notes = "查询所有最近2公里未接订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="get2unbills",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> search2Unbills(@RequestParam(value = "lng") String lng,@RequestParam(value = "lat") String lat,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
//                List<SysBill> billList = billService.selectBillsByLnglat(lng,lat); // 方法1
                List<SysBill> billList = billService.selectBillsIn2Mills(lng,lat);   // 方法2
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1,2
    @ApiOperation(value = "支付接口", notes = "支付接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="paybill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> payBill(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                //先支付
                Date datetime = new Date();
                boolean flag =  billService.payBill(datetime,id);
                if(flag){
                    String unbillstr = redisService.get("unbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<SysBill> unbilllist = billService.selectAllUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("unbilllist");
                            redisService.set("unbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<SysBill> unbilllist = JSONUtil.jsonToList(unbillstr,SysBill.class);
                    for(SysBill sb : unbilllist){
                        if(sb.getId() == id){
                            sb.setBill_status(3);
                        }
                    }
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("unbilllist");
                    redisService.set("unbilllist", unbillliststr);
                    r.setCode("200");
                    r.setMsg("支付成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode("500");
                    r.setMsg("支付失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,4
    @ApiOperation(value = "运单完成", notes = "运单完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "company_code", value = "运单号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "delivery_fee", value = "运费", required = true, dataType = "Number",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="finishbill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> finishBill(@RequestParam(value = "id") long id,@RequestParam(value = "company_code") String company_code,@RequestParam(value = "delivery_fee") Double delivery_fee, HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                Date date = new Date();
                boolean flag =  billService.finishBill(date,id,company_code,delivery_fee);
                if(flag){
                    List<SysBill> unbilllist = billService.selectAllUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("unbilllist");
                        redisService.set("unbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r.setCode("200");
                    r.setMsg("运单完成！");
                    r.setData(null);
                    r.setSuccess(true);
                }else{
                    r.setCode("500");
                    r.setMsg("完成运单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1,2,3,4
    @ApiOperation(value = "收件人名称或电话统一查询订单接口(分页)", notes = "收件人名称或电话统一查询订单接口(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startitem", value = "startitem", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "pagesize", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "isfinishflag", value = "是否为完成的订单标志（0：未完成，1：完成）", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "sender_param", value = "名称或电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getbillbynameortel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillByNameOrTel(@RequestParam(value = "startitem") int startitem,@RequestParam(value = "pagesize") int pagesize,@RequestParam(value = "isfinishflag") int isfinishflag,@RequestParam(value = "sender_param") String sender_param,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                Map<String, Object> map = null;
                if(isfinishflag == 0){
                    if(roleid.equals("2")){
                        //商户
                        map = billService.selectShoperUnfinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("3")){
                        //承运员
                        map = billService.selectTransUnfinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("4")){
                        //物流公司
                        map = billService.selectCompanyUnfinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }
                }else if(isfinishflag == 1){
                    if(roleid.equals("2")){
                        //商户
                        map = billService.selectShoperFinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("3")){
                        //承运员
                        map = billService.selectTransFinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }else if(roleid.equals("4")){
                        //物流公司
                        map = billService.selectCompanyFinishBillByTelOrName(wxuserid,sender_param,startitem,pagesize);
                    }
                }
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(map);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,3
    @ApiOperation(value = "承运员接单接口", notes = "接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="receivebill",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public ResponseEntity<JsonResult> updateBillSetTrans_id(@RequestParam(value = "id") long id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        int availablePermits = semaphore.availablePermits();
        if(availablePermits>0){
//            System.out.println("抢单成功！");
        }else{
//            System.out.println("抢单失败！");
            r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
            r.setData(null);
            r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }
        try {
            semaphore.acquire(1);
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                SysBill bill = billService.selectSingleBill(id);
                if(bill.getTrans_id()!=-1){
                    r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
                    r.setData(null);
                    r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
                    r.setSuccess(false);
                }
                Date datetime = new Date();
                WxUser wxuser = userService.getWxUser(openid);
                boolean flag = billService.updateBillSetTrans_id(id,datetime,wxuser.getId(),wxuser.getNickname());
                if (flag) {
                    String unbillstr = redisService.get("unbilllist");
                    if(unbillstr==null || unbillstr==""){
                        List<SysBill> unbilllist = billService.selectAllUnBills();
                        try {
                            unbillstr = JSONUtil.listToJson(unbilllist);
                            redisService.remove("unbilllist");
                            redisService.set("unbilllist", unbillstr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    List<SysBill> unbilllist = JSONUtil.jsonToList(unbillstr,SysBill.class);
                    for(SysBill sb : unbilllist){
                        if(sb.getId() == id){
                            sb.setBill_status(2);
                        }
                    }
                    String unbillliststr = JSONUtil.listToJson(unbilllist);
                    redisService.remove("unbilllist");
                    redisService.set("unbilllist", unbillliststr);
                    System.out.println("抢单成功！");
                    r.setCode("200");
                    r.setMsg("抢单成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
            r.setSuccess(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }finally {
            semaphore.release(1);
        }
        return ResponseEntity.ok(r);
    }


    //1,3
    @ApiOperation(value = "根据经纬度查询周围所下未完成订单", notes = "根据经纬度查询周围所下未完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "维度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getunfinishbillbyarea",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getUnfinishBillByArea(@RequestParam(value = "lng") String lng,@RequestParam(value = "lat") String lat,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("3")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                List<SysBill> billList = billService.selectBillsByLnglat(lng,lat);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    //1,4
    @ApiOperation(value = "物流公司查询本公司所有已完成订单", notes = "物流公司查询本公司所有已完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "物流公司名称", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getfinishedbillbycompanyid",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getFinishedBillByCompanyId(@RequestParam(value = "company_id") Integer company_id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                List<SysBill> billList = billService.selectfinishedBillByCompanyId(company_id);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //1,4
    @ApiOperation(value = "物流公司查询本公司所有未完成订单", notes = "物流公司查询本公司所有未完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "物流公司名称", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getunfinishedbillbycompanyid",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getUnFinishedBillByCompanyId(@RequestParam(value = "company_id") Integer company_id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("4")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                List<SysBill> billList = billService.selectunfinishedBillByCompanyId(company_id);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(billList);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    public JsonResult ConnectRedisCheckToken(String token){
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry<=5){
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


    //得到img
    @ApiOperation(value = "获取运单图片", notes = "获取运单图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getImg",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getImg(@RequestParam(value = "id") Integer id,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                redisService.expire(token, Constant.expire.getExpirationTime());
                SysBill bill = billService.selectSingleBill(id);

                //region 解析html
                //                URL url = new URL("http://localhost:8080/transport/billvoice.html?id="+id+"&token="+token);
//                URLConnection connection = url.openConnection();
//                connection.setDoOutput(true);
//
//                //若为post请求
////                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"8859_1");
////                out.write("username=xxx&password=ooooo"); //请求
////                out.flush();
////                out.close();
//
//                //得到响应
//                InputStream inputStream;
//                inputStream = connection.getInputStream();
//                String sCurrentline = "";
//                String sTotalstring = "";
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                while ((sCurrentline = reader.readLine())!=null){
//                    sTotalstring += sCurrentline  + "\r\n";
//                }

//                String url = "http://localhost:8080/transport/billvoice.html?id="+id+"&token="+token;
//                org.jsoup.nodes.Document doc = Jsoup.parse(new URL(url),30000);
//                org.jsoup.nodes.Document doc = Jsoup.parse(sTotalstring);
//                org.jsoup.nodes.Element row = doc.body();
//                System.out.println(doc);


//                WebClient webClient = new WebClient();
//                webClient.getOptions().setJavaScriptEnabled(false);
//                webClient.getOptions().setCssEnabled(false);
//                webClient.getOptions().setUseInsecureSSL(false);
//                //获取页面
//                HtmlPage page = webClient.getPage(url);
//                System.out.println("页面文本:"+page.asText());

//                String cmd = "rundll32 url.dll,FileProtocolHandler "+url;
 //               int a = Runtime.getRuntime().exec(cmd).waitFor();
//                ScriptEngineManager sem = new ScriptEngineManager();
//                ScriptEngine se = sem.getEngineByName("javascript"); //初始化Java内置的javascript引擎
//                try {
//                    se.put("document",doc); //关联对象，这一步很重要，关联javascript的document对象为TaoDocument，亦即我自己实现的document对象
//                    se.eval("function test() { return 'base';}");
//                    Invocable invocableEngine = (Invocable) se;//转换引擎类型为Invocable
//                    Element callbackvalue=(Element) invocableEngine.invokeFunction("test"); //直接运行函数，返回值为Element
//                    System.out.println("callback return :"+callbackvalue); //打印输出返回内容
////                    se.eval("test()");//另外一种调用函数方式，我更偏爱此种方式
//                } catch (ScriptException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                //endregion

                CompanyLines companyLines = companyLinesMapper.selectByPrimaryKey(bill.getLine_id());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String finish_time = formatter.format(bill.getFinish_time());
                String bigtitle = bill.getCompany_name()+"托运单";
                List<List<List<String>>> allValue = new ArrayList<>();
                List<String> content1 = Arrays.asList(new String[]{bill.getRec_name(),bill.getRec_tel(),bill.getRec_procity(),bill.getRec_detailarea()});
                List<String> content2 = Arrays.asList(new String[]{bill.getSender_name(),bill.getSender_tel(),bill.getSender_procity(),bill.getSender_detailarea()});
                List<String> content3 = Arrays.asList(new String[]{bill.getCompany_code(),bill.getGoodsname(),bill.getShop_name(),companyLines.getArriveTel(),finish_time});
                List<String> h4 = Arrays.asList(new String[]{"物流线路","数量","到站地址","物流公司运费","备注"});
                List<String> content4 = Arrays.asList(new String[]{companyLines.getBeginAddr()+"-->"+companyLines.getArriveAddr(),bill.getGoodsnum()+"",companyLines.getArriveAddr(),bill.getDelivery_fee()+"",bill.getBillinfo()});
                List<List<String>> contentArray1 = new ArrayList<>();
                contentArray1.add(content1);
                List<List<String>> contentArray2 = new ArrayList<>();
                contentArray2.add(content2);
                List<List<String>> contentArray3 = new ArrayList<>();
                contentArray3.add(content3);
                contentArray3.add(h4);
                contentArray3.add(content4);

                allValue.add(contentArray1);
                allValue.add(contentArray2);
                allValue.add(contentArray3);

                List<String[]> headTitles = new ArrayList<>();
                String[] h1 = new String[]{"姓名","电话","省市区","详细地址"};
                String[] h2 = new String[]{"姓名","电话","省市区","详细地址"};
                String[] h3 = new String[]{"运单号","品名","店铺名称","到站电话","日期"};
                headTitles.add(h1);
                headTitles.add(h2);
                headTitles.add(h3);

                List<String> titles = new ArrayList<>();
                titles.add("收件人信息");
                titles.add("寄件人信息");
                titles.add("运单信息");
                BufferedImage image = graphicsutils.graphicsGeneration(allValue,titles,headTitles ,"",4,bigtitle);
                //旋转90度
                image = ImageUtil.rotateImage(image,90);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
                ImageIO.write(image, "png", baos);//写入流中
                byte[] bytes = baos.toByteArray();//转换成字节
                BASE64Encoder encoder = new BASE64Encoder();
                String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
                png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n

                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(png_base64);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @RequestMapping(value="pushbase",method = RequestMethod.POST)
    @ResponseBody
    public String getImg(@RequestParam(value = "base") String base,HttpServletRequest request){
        String id = request.getHeader("id");
        if(id!=null && id!=""){
            imgBases.put(id,base);
        }
        return "success";
    }

    @ApiOperation(value = "商家批量下单点击合并下单按钮", notes = "商家批量下单点击合并下单按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="savebatchbills",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> saveBatchBills(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                String batch_code = df.format(new Date()) + UUID.randomUUID().toString().substring(0,5);
                boolean flag = billService.updateBatchBillsCode(wxuserid,batch_code);
                if (flag) {
                    List<SysBill> unbilllist = billService.selectAllUnBills();
                    try {
                        String unbillstr = JSONUtil.listToJson(unbilllist);
                        redisService.remove("unbilllist");
                        redisService.set("unbilllist", unbillstr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    r.setCode("200");
                    r.setMsg("下单成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else {
                    r.setCode("500");
                    r.setMsg("下单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_CREATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_CREATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "商家批量下单", notes = "商家批量下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="createbatchbills",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> createBatchBills(@RequestBody List<SysBill_s> billlist, HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);

                //创建订单
                SysBill sysBill = null;
                boolean flag = true;
                for(int i = 0;i<billlist.size();i++){
                    sysBill = new SysBill();
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                    String bill_code = df.format(new Date()) + UUID.randomUUID().toString().substring(0,5);
                    sysBill.setBill_code(bill_code);
                    sysBill.setSender_id(wxuserid);    //wxuserid即为商户的sender_id
                    sysBill.setGoodsname(billlist.get(i).getGoodsname());
                    sysBill.setGoodsnum(billlist.get(i).getGoodsnum());
                    sysBill.setSender_name(billlist.get(i).getSender_name());
                    sysBill.setSender_tel(billlist.get(i).getSender_tel());
                    sysBill.setShop_id(billlist.get(i).getShop_id());
                    sysBill.setShop_name(billlist.get(i).getShop_name());
                    sysBill.setCompany_id(billlist.get(i).getCompany_id());
                    sysBill.setCompany_name(billlist.get(i).getCompany_name());
                    sysBill.setBatch_code("");
                    sysBill.setBill_status(1);
                    sysBill.setTrans_id(-1);
                    sysBill.setSender_lat(billlist.get(i).getSender_lat());
                    sysBill.setSender_lng(billlist.get(i).getSender_lng());
                    sysBill.setBillinfo(billlist.get(i).getBillinfo());
                    sysBill.setSender_procity(billlist.get(i).getSender_procity());
                    sysBill.setSender_detailarea(billlist.get(i).getSender_detailarea());
                    sysBill.setRec_name(billlist.get(i).getRec_name());
                    sysBill.setRec_tel(billlist.get(i).getRec_tel());
                    sysBill.setRec_procity(billlist.get(i).getRec_procity());
                    sysBill.setRec_detailarea(billlist.get(i).getRec_detailarea());
                    sysBill.setPrice(billlist.get(i).getPrice());
                    sysBill.setCompany_code(billlist.get(i).getCompany_code());
                    sysBill.setCreate_time(new Date());

                    sysBill.setLine_id(billlist.get(i).getLine_id());
                    sysBill.setPay_method(billlist.get(i).getPay_method());
                    sysBill.setGive_method(billlist.get(i).getGive_method());
                    sysBill.setKeepfee(billlist.get(i).getKeepfee());
                    sysBill.setWaitnote(billlist.get(i).getWaitnote());

                    flag = flag && billService.insertBill(sysBill);
                }
                if (flag && billlist.size()>0) {
                    //是否需要加入redis ？？？
                    r.setCode("200");
                    r.setMsg("下单成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }else {
                    r.setCode("500");
                    r.setMsg("下单失败！");
                    r.setData(null);
                    r.setSuccess(false);
                }
            }else{
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_CREATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_CREATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }



    public String getAdminToken(){
        User user = userService.getUserByLoginName("system");
        String admintoken  = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }
}
