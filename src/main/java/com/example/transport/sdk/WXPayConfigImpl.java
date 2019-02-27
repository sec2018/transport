package com.example.transport.sdk;

//微信支付配置类
import com.example.transport.service.Constant;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {
    private String appId = Constant.WX_APP_ID;
    private String  mchId = Constant.WX_SHOP_ID;//商户号
    private String key = Constant.WX_SHOP_KEY;//密钥key

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;

    public WXPayConfigImpl() throws Exception{
        String certPath = "D:\\WXResource\\apiclient_cert.p12";//证书位置
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
        return appId;
    }

    public String getMchID() {
        return mchId;
    }

    public String getKey() {
        return key;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
