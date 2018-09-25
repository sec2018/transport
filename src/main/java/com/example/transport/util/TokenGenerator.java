package com.example.transport.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 生成 token
 */
public class TokenGenerator {
    public static String generateValue(){
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] hexcode="0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data){
        if(data==null){
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);

        for(byte b:data){
            r.append(hexcode[(b>>4) & 0xF]);
            r.append(hexcode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateValue(String param){
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("生成token失败");
            e.printStackTrace();
        }
        return "";
    }
}
