package com.example.transport.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    /**
     * 秘钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    /**
     * SecretKeySpec类是KeySpec接口的实现类，用于构建秘钥规范
     */
    private SecretKeySpec key;

    private static String sKey = "1234567890123456";
    static byte[]  srcIv=new byte[16];

    public AESUtil() {
        key = new SecretKeySpec(sKey.getBytes(), ALGORITHM);
    }
    /**
     * hex字符串转byte数组
     * @param s
     * @return
     */
    private static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte (s.getBytes(), 0, s.length() >> 1);
        } else {
            return hex2byte("0"+s);
        }
    }

    private static byte[] hex2byte (byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i=0; i<len*2; i++) {
            int shift = i%2 == 1 ? 0 : 4;
            d[i>>1] |= Character.digit((char) b[offset+i], 16) << shift;
        }
        return d;
    }

    public static void main(String[] args) throws Exception {
        AESUtil util = new AESUtil();
        System.out.println("cardNo:"+util.encryptData("123"));
        System.out.println("exp:"+util.decryptData("euLUpj0cPhoYeh/Yn0ce9Q=="));
    }

    /**将二进制转换为16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    //加密
    public static String encryptData(String sSrc) throws Exception{
        if(sKey == null || sKey.length() != 16){
            throw new Exception("sKey长度异常");
        }
        byte[] raw=sKey.getBytes();
        SecretKeySpec skeySpec=new SecretKeySpec(raw,"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(srcIv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return new BASE64Encoder().encode(encrypted);

    }

    //解密
    public static String decryptData(String sSrc) throws Exception {
        try {
            //判断Key是否正确
            if (sKey == null || sKey .length() != 16) {
                throw new Exception("sKey长度异常");
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(srcIv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}

