package com.example.transport.pojo;

public class SysBill {
    private long id;
    private String bill_code;
    private long sender_id;
    private String sender_name;
    private String sender_tel;
    private String shop_name;
    private long company_id;
    private String company_name;
    private long trans_id;
    private String goodsname;
    private int goodsnum;
    private String billinfo;

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

    @Override
    public String toString() {
        return "SysBill{" +
                "id=" + id +
                ", bill_code='" + bill_code + '\'' +
                ", sender_id=" + sender_id +
                ", sender_name='" + sender_name + '\'' +
                ", sender_tel='" + sender_tel + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", company_id=" + company_id +
                ", company_name='" + company_name + '\'' +
                ", trans_id=" + trans_id +
                ", trans_name='" + trans_name + '\'' +
                ", batch_code='" + batch_code + '\'' +
                ", bill_status=" + bill_status +
                ", sender_lat='" + sender_lat + '\'' +
                ", sender_lng='" + sender_lng + '\'' +
                '}';
    }

    private String trans_name;
    private String batch_code;
    private int bill_status;
    private String sender_lat;
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

}
