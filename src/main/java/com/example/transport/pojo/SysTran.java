package com.example.transport.pojo;

public class SysTran {
    private Integer id;

    private Long wxuserId;

    private String tranName;

    private String tranTel;

    private String idFrontUrl;

    private String idBackUrl;

    public SysTran(Integer id, Long wxuserId, String tranName, String tranTel, String idFrontUrl, String idBackUrl) {
        this.id = id;
        this.wxuserId = wxuserId;
        this.tranName = tranName;
        this.tranTel = tranTel;
        this.idFrontUrl = idFrontUrl;
        this.idBackUrl = idBackUrl;
    }

    public SysTran() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getWxuserId() {
        return wxuserId;
    }

    public void setWxuserId(Long wxuserId) {
        this.wxuserId = wxuserId;
    }

    public String getTranName() {
        return tranName;
    }

    public void setTranName(String tranName) {
        this.tranName = tranName == null ? null : tranName.trim();
    }

    public String getTranTel() {
        return tranTel;
    }

    public void setTranTel(String tranTel) {
        this.tranTel = tranTel == null ? null : tranTel.trim();
    }

    public String getIdFrontUrl() {
        return idFrontUrl;
    }

    public void setIdFrontUrl(String idFrontUrl) {
        this.idFrontUrl = idFrontUrl == null ? null : idFrontUrl.trim();
    }

    public String getIdBackUrl() {
        return idBackUrl;
    }

    public void setIdBackUrl(String idBackUrl) {
        this.idBackUrl = idBackUrl == null ? null : idBackUrl.trim();
    }
}