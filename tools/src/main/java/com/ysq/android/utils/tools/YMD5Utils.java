package com.ysq.android.utils.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ysq on 16/8/8.
 */
public class YMD5Utils {
    /**
     * 对字符串进行MD5加密
     *
     * @param data 源字符串
     * @return 加密后的字符串
     */
    public static String encryptMD5(String data) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            return new BigInteger(md5.digest(data.getBytes())).toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
