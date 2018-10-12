package com.example.transport.api;

import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("api")
@Controller
public class SenderBillApi {

    @Autowired
    private BillService billService;

    @ApiOperation(value = "商家下单接口", notes = "下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender_id", value = "商家id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "sender_name", value = "商家名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "商家电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_name", value = "商铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "company_id", value = "物流公司id", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "company_name", value = "物流公司名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "batch_code", value = "批量下单编号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lat", value = "经度", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "lng", value = "维度", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "createbill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> createBill(@RequestParam(value = "sender_id") long sender_id, @RequestParam(value = "sender_name")String sender_name, @RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "batch_code")String batch_code,@RequestParam(value = "lat")String lat,@RequestParam(value = "lng")String lng) {
        JsonResult r = new JsonResult();
        try {
            //创建订单
            SysBill sysBill = new SysBill();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            String bill_code = df.format(new Date()) + UUID.randomUUID();
            sysBill.setBill_code(bill_code);
            sysBill.setSender_id(sender_id);
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
            boolean flag = billService.insertBill(sysBill);
            if (flag) {
                r.setCode("200");
                r.setMsg("下单成功！");
                r.setData(null);
                r.setSuccess(true);
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


    @ApiOperation(value = "更新订单接口", notes = "更新订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "sender_tel", value = "商家电话", required = true, dataType = "String",paramType = "query"),
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
    @ResponseBody
    public ResponseEntity<JsonResult> updateBillSetTrans_id(@RequestParam(value = "id") long id,@RequestParam(value = "trans_id") long trans_id,@RequestParam(value = "bill_status") int bill_status){
        JsonResult r = new JsonResult();
        try {
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
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
