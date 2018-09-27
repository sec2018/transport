package com.example.transport.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("senderbill")
@Controller
public class SenderBillController {

    @Autowired
    private BillService billService;

    @RequestMapping("create")
    @Transactional
    public JSONObject createBill(String bill_code, Long sender_id, String sender_name, String send_tel,
                                 String shop_name, int company_id, String company_name, Long trans_id,
                                 String varchar, String batch_code, int bill_status) {
        JSONObject obj = new JSONObject();
        obj.put("status", "下单失败！");
        try {
            //创建订单
            SysBill sysBill = new SysBill();

            boolean flag = billService.insertBill(sysBill);
            if (flag) {
                obj.put("status", "下单成功！");
                return obj;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return obj;
        }
        return obj;
    }
}
