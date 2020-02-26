package com.tcl.jr.ebank.webService.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {

    private SHAUtil() {
    }

    /**
     * SHA签名
     *
     * @param inputStr
     * @return
     */
    public static String getSHA1Code(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");  //可取的值有MD2 MD5 SHA-1 SHA-256 SHA-384 SHA0-512
        messageDigest.reset();
        messageDigest.update(inputStr.getBytes("UTF-8"));
        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    /**
     * SHA验签
     *
     * @param data
     * @param signature
     * @return
     */
    public static boolean verifySignature(String data, String signature) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return signature.equals(getSHA1Code(data));
    }
}
