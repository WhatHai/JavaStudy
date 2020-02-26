package com.tcl.jr.ebank.webService.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Util {
    private Base64Util(){}
    //加密
    public static String encryptBASE(byte[] key) {
        return (new BASE64Encoder()).encode(key);
    }

    //解密

    public static byte[] decryptBASE(String key)  throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
}
