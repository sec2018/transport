package com.example.transport.api;

import com.example.transport.pojo.SysBill;
import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
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

    Semaphore semaphore = new Semaphore(1);

    @ApiOperation(value = "商家下单接口", notes = "下单")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "sender_name", value = "下单人名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsname", value = "商品名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "goodsnum", value = "商品数量", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "商家电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_id", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
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
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "createbill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> createBill(@RequestParam(value = "sender_name")String sender_name,@RequestParam(value = "goodsname")String goodsname, @RequestParam(value = "goodsnum")Integer goodsnum,@RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "batch_code")String batch_code,@RequestParam(value = "lat")String lat,@RequestParam(value = "lng")String lng,@RequestParam(value = "billinfo")String billinfo,
                        @RequestParam(value = "sender_procity")String sender_procity,@RequestParam(value = "sender_detailarea")String sender_detailarea,@RequestParam(value = "rec_name")String rec_name,
                        @RequestParam(value = "rec_tel")String rec_tel,@RequestParam(value = "rec_procity")String rec_procity,@RequestParam(value = "rec_detailarea")String rec_detailarea,HttpServletRequest request) {
        String token = request.getHeader("token");
        JsonResult r = new JsonResult();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }

        try {
            if(tokenvalue != null){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);

                //创建订单
                SysBill sysBill = new SysBill();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                String bill_code = df.format(new Date()) + UUID.randomUUID();
                sysBill.setBill_code(bill_code);
                sysBill.setSender_id(wxuserid);    //wxuserid即为商户的sender_id
                sysBill.setGoodsname(goodsname);
                sysBill.setGoodsnum(goodsnum);
                sysBill.setSender_name(sender_name);
                sysBill.setSender_tel(sender_tel);
                sysBill.setShop_name(shop_name);
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
                boolean flag = billService.insertBill(sysBill);
                if (flag) {
                    r.setCode("200");
                    r.setMsg("下单成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r.setCode(Constant.TOKEN_ERROR.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.TOKEN_ERROR.getMsg());
                r.setSuccess(false);
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

    @ApiOperation(value = "商家查询所下订单", notes = "根据商家标识查询所下订单")
    @ApiImplicitParam(name = "sender_id", value = "商家id", required = true, dataType = "Long",paramType = "query")
    @RequestMapping(value="getuserbill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillByUserId(@RequestParam(value = "sender_id") long sender_id){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectUserBill(sender_id);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败！");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "通过id查询订单", notes = "根据订单标识查询订单")
    @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query")
    @RequestMapping(value="getbill", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillById(@RequestParam(value = "id") long id){
        JsonResult r = new JsonResult();
        try {
            SysBill bill = billService.selectSingleBill(id);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(bill);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败！");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "更新订单接口", notes = "更新订单")          //让修改哪些内容？
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "下单人电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_id", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "company_name", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "trans_id", value = "承运员id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "trans_name", value = "承运员名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "batch_code", value = "批量下单编号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "bill_status", value = "订单状态（1,2,3,4）", required = true, dataType = "Integer",paramType = "query")
    })
    @RequestMapping(value="updatebill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> updateBill(@RequestParam(value = "id") long id, @RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "trans_id")Long trans_id,@RequestParam(value = "trans_name")String trans_name, @RequestParam(value = "batch_code")String batch_code,
                        @RequestParam(value = "bill_status")int bill_status){
        SysBill sysBill = billService.selectSingleBill(id);
        JsonResult r = new JsonResult();
        try {
            sysBill.setSender_tel(sender_tel);
            sysBill.setShop_name(shop_name);
            sysBill.setCompany_id(company_id);
            sysBill.setCompany_name(company_name);
            sysBill.setTrans_id(trans_id);
            sysBill.setTrans_name(trans_name);
            sysBill.setBatch_code(batch_code);
            sysBill.setBill_status(bill_status);
            boolean flag = billService.updateBill(sysBill);
            if (flag) {
                r.setCode("200");
                r.setMsg("更新成功！");
                r.setData(null);
                r.setSuccess(true);
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_UPDATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_UPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "更新订单状态接口", notes = "根据id更新订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "bill_status", value = "订单状态（1,2,3,4）", required = true, dataType = "Integer",paramType = "query")
    })
    @RequestMapping(value="updatebillstatus",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> updateBillStatus(@RequestParam(value = "id") long id,@RequestParam(value = "bill_status") int bill_status){
        JsonResult r = new JsonResult();
        try {
            boolean flag = billService.updateBillStatus(bill_status,id);
            if (flag) {
                if (flag) {
                    r.setCode("200");
                    r.setMsg("更新成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }
        } catch (Exception e) {
            r.setCode(Constant.BILL_UPDATEFAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.BILL_UPDATEFAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "删除订单接口", notes = "根据id删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query")
    })
    @RequestMapping(value="deletebill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> deleteBillById(@RequestParam(value = "id") long id){
        JsonResult r = new JsonResult();
        try {
            boolean flag = billService.deleteBill(id);
            if (flag) {
                r.setCode("200");
                r.setMsg("删除成功！");
                r.setData(null);
                r.setSuccess(true);
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


    @ApiOperation(value = "商家查询所下未完成订单", notes = "根据商家标识查询所下未完成订单")
    @ApiImplicitParam(name = "sender_id", value = "商家id", required = true, dataType = "Long",paramType = "query")
    @RequestMapping(value="getuserunfinishbill",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchunfinishBillByUserId(@RequestParam(value = "sender_id") long sender_id){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectUnfinishBill(sender_id);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "根据名称和电话查询订单", notes = "根据名称和电话查询订单")
    @ApiImplicitParam(name = "sender_param", value = "名称或电话", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value="getbillbynameortel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchBillByNameOrTel(@RequestParam(value = "sender_param") String sender_param){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectUnfinishBillByTelOrName(sender_param);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "承运员接单接口", notes = "接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "trans_id", value = "承运员id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "bill_status", value = "订单状态（1,2,3,4）", required = true, dataType = "Integer",paramType = "query")
    })
    @RequestMapping(value="receivebill",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public ResponseEntity<JsonResult> updateBillSetTrans_id(@RequestParam(value = "id") long id,@RequestParam(value = "trans_id") long trans_id,@RequestParam(value = "bill_status") int bill_status){
        int availablePermits = semaphore.availablePermits();
        JsonResult r = new JsonResult();
        if(availablePermits>0){
            System.out.println("抢单成功！");
        }else{
            System.out.println("抢单失败！");
            r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
            r.setData(null);
            r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }
        try {
            semaphore.acquire(1);
            SysBill bill = billService.selectSingleBill(id);
            if(bill.getTrans_id()!=-1){
                r.setCode(Constant.BILL_RECEIVEFAILURE.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.BILL_RECEIVEFAILURE.getMsg());
                r.setSuccess(false);
            }
            boolean flag = billService.updateBillSetTrans_id(id,bill_status,trans_id);
            if (flag) {
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(null);
                r.setSuccess(true);
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



    @ApiOperation(value = "根据经纬度查询周围所下未完成订单", notes = "根据经纬度查询周围所下未完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lng", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "维度", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="getunfinishbillbyarea",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getUnfinishBillByArea(@RequestParam(value = "lng") String lng,@RequestParam(value = "lat") String lat){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectBillsByLnglat(lng,lat);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "物流公司查询本公司所有已完成订单", notes = "物流公司查询本公司所有已完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "物流公司名称", required = true, dataType = "Integer",paramType = "query")
    })
    @RequestMapping(value="getfinishedbillbycompanyid",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getFinishedBillByCompanyId(@RequestParam(value = "company_id") Integer company_id){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectfinishedBillByCompanyId(company_id);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "物流公司查询本公司所有未完成订单", notes = "物流公司查询本公司所有未完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_id", value = "物流公司名称", required = true, dataType = "Integer",paramType = "query")
    })
    @RequestMapping(value="getunfinishedbillbycompanyid",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> getUnFinishedBillByCompanyId(@RequestParam(value = "company_id") Integer company_id){
        JsonResult r = new JsonResult();
        try {
            List<SysBill> billList = billService.selectunfinishedBillByCompanyId(company_id);
            r.setCode("200");
            r.setMsg("查询成功！");
            r.setData(billList);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg("查询失败");
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

}
