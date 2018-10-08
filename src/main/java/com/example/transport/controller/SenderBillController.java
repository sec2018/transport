package com.example.transport.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.transport.pojo.SysBill;
import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.BillService;
import com.example.transport.service.Constant;
import com.example.transport.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("api")
@Controller
public class SenderBillController {

    @Autowired
    private BillService billService;

    @RequestMapping("createbill")
    @Transactional
    @ResponseBody
    public R createBill(@RequestParam(value = "sender_id") long sender_id, @RequestParam(value = "sender_name")String sender_name, @RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "trans_id")Long trans_id,@RequestParam(value = "trans_name")String trans_name, @RequestParam(value = "batch_code")String batch_code,
                        @RequestParam(value = "bill_status")int bill_status) {
        R r = new R();
        r.put("code",Constant.BILL_CREATEFAILURE.getCode()).put("msg",Constant.BILL_CREATEFAILURE.getMsg());
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
            sysBill.setTrans_id(trans_id);
            sysBill.setTrans_name(trans_name);
            sysBill.setBatch_code(batch_code);
            sysBill.setBill_status(bill_status);
            boolean flag = billService.insertBill(sysBill);
            if (flag) {
                r.put("code",Constant.BILL_CREATESUCCESS.getCode()).put("msg",Constant.BILL_CREATESUCCESS.getMsg());
                return r;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return r;
        }
        return r;
    }


    @RequestMapping("getuserbill")
    @ResponseBody
    public R searchBillByUserId(@RequestParam(value = "sender_id") long sender_id){
//        R r = new R();
//        r.put("code",Constant.BILL_SEARCHFAILURE.getCode()).put("msg",Constant.BILL_SEARCHFAILURE.getMsg());
        List<SysBill> billList = billService.selectUserBill(sender_id);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",billList);
        return R.ok(map).put("code",200);
    }

    @RequestMapping("getbill")
    @ResponseBody
    public R searchBillById(@RequestParam(value = "id") long id){
        SysBill bill = billService.selectSingleBill(id);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",bill);
        return R.ok(map).put("code",200);
    }

    @RequestMapping("updatebill")
    @Transactional
    @ResponseBody
    public R updateBill(@RequestParam(value = "id") long id, @RequestParam(value = "sender_tel")String sender_tel,
                        @RequestParam(value = "shop_name")String shop_name, @RequestParam(value = "company_id")int company_id, @RequestParam(value = "company_name")String company_name,
                        @RequestParam(value = "trans_id")Long trans_id,@RequestParam(value = "trans_name")String trans_name, @RequestParam(value = "batch_code")String batch_code,
                        @RequestParam(value = "bill_status")int bill_status){
        SysBill sysBill = billService.selectSingleBill(id);

        R r = new R();
        r.put("code",Constant.BILL_UPDATEFAILURE.getCode()).put("msg",Constant.BILL_UPDATEFAILURE.getMsg());
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
                r.put("code",Constant.BILL_UPDATESUCCESS.getCode()).put("msg",Constant.BILL_UPDATESUCCESS.getMsg());
                return r;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return r;
        }
        return r;
    }

    @RequestMapping("updatebillstatus")
    @Transactional
    @ResponseBody
    public R updateBillStatus(@RequestParam(value = "bill_status") int bill_status,@RequestParam(value = "id") long id){
        R r = new R();
        r.put("code",Constant.BILL_UPDATEFAILURE.getCode()).put("msg",Constant.BILL_UPDATEFAILURE.getMsg());
        try {
            boolean flag = billService.updateBillStatus(bill_status,id);
            if (flag) {
                r.put("code",Constant.BILL_UPDATESUCCESS.getCode()).put("msg",Constant.BILL_UPDATESUCCESS.getMsg());
                return r;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return r;
        }
        return r;
    }

    @RequestMapping("deletebill")
    @Transactional
    @ResponseBody
    public R deleteBillById(@RequestParam(value = "id") long id){
        R r = new R();
        r.put("code",Constant.BILL_DELETEFAILURE.getCode()).put("msg",Constant.BILL_DELETEFAILURE.getMsg());
        try {
            boolean flag = billService.deleteBill(id);
            if (flag) {
                r.put("code",Constant.BILL_DELETESUCCESS.getCode()).put("msg",Constant.BILL_DELETESUCCESS.getMsg());
                return r;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return r;
        }
        return r;
    }
}
