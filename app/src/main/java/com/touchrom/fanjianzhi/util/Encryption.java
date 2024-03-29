package com.touchrom.fanjianzhi.util;

import com.arialyy.frame.util.CalendarUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 加密工具
 * Created by lyy on 14-5-14.
 */
public class Encryption {
    public final static String APP_SECRET = "C87E3B96CFE867AF1D77C5";

    private static String initRandomCode() {
        return CalendarUtils.formatStringWithDate(new Date(), "yyyyMMDDHHmmss");
    }

    /**
     * 随机数：yyyyMMDDHHmmss格式的日期
     *
     * @return
     */
    public static String getRandomCode() {
        return initRandomCode();
    }

    public static String getSingCode(String randomCode, String timestamp) {
        return encodeSHA1ToString(APP_SECRET + randomCode + timestamp);
    }

    public static byte[] encodeSha1(String str) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            return sha1.digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeMD5(String strSrc) {
        byte[] returnByte = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            returnByte = md5.digest(strSrc.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnByte;
    }

    public static String encodeMD5ToString(String src) {
        return bytesToHexString(encodeMD5(src));
    }

    public static String encodeSHA1ToString(String src) {
        return bytesToHexString(encodeSha1(src));
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
