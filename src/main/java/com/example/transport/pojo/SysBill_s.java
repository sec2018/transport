package com.example.transport.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "SysBill", description = "用户对象")
public class SysBill_s {

    @ApiModelProperty(value = "id")
    private long id;
    @ApiModelProperty(value = "bill_code")
    private String bill_code;
    @ApiModelProperty(value = "sender_id")
    private long sender_id;
    @ApiModelProperty(value = "sender_name")
    private String sender_name;
    @ApiModelProperty(value = "sender_tel")
    private String sender_tel;
    @ApiModelProperty(value = "shop_id")
    private int shop_id;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    @ApiModelProperty(value = "shop_name")
    private String shop_name;
    @ApiModelProperty(value = "company_id")
    private long company_id;
    @ApiModelProperty(value = "company_name")
    private String company_name;
    @ApiModelProperty(value = "trans_id")
    private long trans_id;
    @ApiModelProperty(value = "goodsname")
    private String goodsname;
    @ApiModelProperty(value = "goodsnum")
    private int goodsnum;
    @ApiModelProperty(value = "billinfo")
    private String billinfo;
    @ApiModelProperty(value = "sender_procity")
    private String sender_procity;
    @ApiModelProperty(value = "sender_detailarea")
    private String sender_detailarea;
    @ApiModelProperty(value = "rec_name")
    private String rec_name;
    @ApiModelProperty(value = "rec_tel")
    private String rec_tel;
    @ApiModelProperty(value = "rec_procity")
    private String rec_procity;
    @ApiModelProperty(value = "rec_detailarea")
    private String rec_detailarea;
    @ApiModelProperty(value = "price")
    private double price;
    @ApiModelProperty(value = "create_time")
    private Date create_time;
    @ApiModelProperty(value = "rec_time")
    private Date rec_time;
    @ApiModelProperty(value = "pay_time")
    private Date pay_time;
    @ApiModelProperty(value = "finish_time")
    private Date finish_time;
    @ApiModelProperty(value = "pay_method")
    private int pay_method;
    @ApiModelProperty(value = "give_method")
    private int give_method;
    @ApiModelProperty(value = "keepfee")
    private double keepfee;
    @ApiModelProperty(value = "waitnote")
    private int waitnote;

    public int getPay_method() {
        return pay_method;
    }

    public void setPay_method(int pay_method) {
        this.pay_method = pay_method;
    }

    public int getGive_method() {
        return give_method;
    }

    public void setGive_method(int give_method) {
        this.give_method = give_method;
    }

    public double getKeepfee() {
        return keepfee;
    }

    public void setKeepfee(double keepfee) {
        this.keepfee = keepfee;
    }

    public int getWaitnote() {
        return waitnote;
    }

    public void setWaitnote(int waitnote) {
        this.waitnote = waitnote;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    private String company_code;

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getRec_time() {
        return rec_time;
    }

    public void setRec_time(Date rec_time) {
        this.rec_time = rec_time;
    }

    public Date getPay_time() {
        return pay_time;
    }

    public void setPay_time(Date pay_time) {
        this.pay_time = pay_time;
    }

    public Date getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Date finish_time) {
        this.finish_time = finish_time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSender_procity() {
        return sender_procity;
    }

    public void setSender_procity(String sender_procity) {
        this.sender_procity = sender_procity;
    }

    public String getSender_detailarea() {
        return sender_detailarea;
    }

    public void setSender_detailarea(String sender_detailarea) {
        this.sender_detailarea = sender_detailarea;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_tel() {
        return rec_tel;
    }

    public void setRec_tel(String rec_tel) {
        this.rec_tel = rec_tel;
    }

    public String getRec_procity() {
        return rec_procity;
    }

    public void setRec_procity(String rec_procity) {
        this.rec_procity = rec_procity;
    }

    public String getRec_detailarea() {
        return rec_detailarea;
    }

    public void setRec_detailarea(String rec_detailarea) {
        this.rec_detailarea = rec_detailarea;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(int goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getBillinfo() {
        return billinfo;
    }

    public void setBillinfo(String billinfo) {
        this.billinfo = billinfo;
    }

    @ApiModelProperty(value = "trans_name")
    private String trans_name;
    @ApiModelProperty(value = "batch_code")
    private String batch_code;
    @ApiModelProperty(value = "bill_status")
    private int bill_status;
    @ApiModelProperty(value = "sender_lat")
    private String sender_lat;
    @ApiModelProperty(value = "sender_lng")
    private String sender_lng;

    public String getSender_lat() {
        return sender_lat;
    }

    public void setSender_lat(String sender_lat) {
        this.sender_lat = sender_lat;
    }

    public String getSender_lng() {
        return sender_lng;
    }

    public void setSender_lng(String sender_lng) {
        this.sender_lng = sender_lng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBill_code() {
        return bill_code;
    }

    public void setBill_code(String bill_code) {
        this.bill_code = bill_code;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_tel() {
        return sender_tel;
    }

    public void setSender_tel(String sender_tel) {
        this.sender_tel = sender_tel;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public long getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(long trans_id) {
        this.trans_id = trans_id;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public int getBill_status() {
        return bill_status;
    }

    public void setBill_status(int bill_status) {
        this.bill_status = bill_status;
    }

    @ApiModelProperty(value = "line_id")
    private Integer line_id;
    @ApiModelProperty(value = "out_trade_no")
    private String out_trade_no;
    @ApiModelProperty(value = "transaction_id")
    private String transaction_id;
    @ApiModelProperty(value = "refundstatus")
    private Integer refundstatus;
    @ApiModelProperty(value = "refundcode")
    private String refundcode;
    @ApiModelProperty(value = "refund_time")
    private Date refund_time;

    public Integer getLine_id() {
        return line_id;
    }

    public void setLine_id(Integer line_id) {
        this.line_id = line_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Integer getRefundstatus() {
        return refundstatus;
    }

    public void setRefundstatus(Integer refundstatus) {
        this.refundstatus = refundstatus;
    }

    public String getRefundcode() {
        return refundcode;
    }

    public void setRefundcode(String refundcode) {
        this.refundcode = refundcode;
    }

    public Date getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(Date refund_time) {
        this.refund_time = refund_time;
    }
}
