package com.example.transport.pojo;

public class SysShop {
    private Integer shopId;

    private String shopName;

    private String shopProcity;

    private String shopDetailarea;

    private Long wxuserId;

    private String shopTel;

    public SysShop(Integer shopId, String shopName, String shopProcity, String shopDetailarea, Long wxuserId, String shopTel) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopProcity = shopProcity;
        this.shopDetailarea = shopDetailarea;
        this.wxuserId = wxuserId;
        this.shopTel = shopTel;
    }

    public SysShop() {
        super();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopProcity() {
        return shopProcity;
    }

    public void setShopProcity(String shopProcity) {
        this.shopProcity = shopProcity == null ? null : shopProcity.trim();
    }

    public String getShopDetailarea() {
        return shopDetailarea;
    }

    public void setShopDetailarea(String shopDetailarea) {
        this.shopDetailarea = shopDetailarea == null ? null : shopDetailarea.trim();
    }

    public Long getWxuserId() {
        return wxuserId;
    }

    public void setWxuserId(Long wxuserId) {
        this.wxuserId = wxuserId;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel == null ? null : shopTel.trim();
    }
}