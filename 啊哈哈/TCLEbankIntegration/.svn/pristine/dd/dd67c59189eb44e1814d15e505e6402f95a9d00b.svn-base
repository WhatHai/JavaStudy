package com.tcl.jr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.jr.ebank.webService.service.EncryptionService;


@RestController
public class EncryptionController {
//    private static final Logger logger = LoggerFactory.getLogger(EncryptionController.class);
//
//    private static final String REC_TEXT_MSG = "收到报文原文:{}";
//    private static final String REC_ENC_MSG = "收到报文密文:{},签名:{},aeskey:{}";
    private static final String BACK_ENC_MSG = "返回密文:{}";
//    private static final String BACK_TEXT_MSG = "返回明文:{}";
    @Autowired
    EncryptionService encryptService;

    @PostMapping(value = "/encrypt", produces = "text/html;charset=UTF-8")
    public String encrypt(String text) {
//        logger.info(REC_TEXT_MSG, text);
    	 System.out.println("aaaaa"+text);
        String encrypted = encryptService.encrypt(text);
//        String encrypted = BACK_ENC_MSG + "";
//        logger.info(BACK_ENC_MSG, encrypted);
//        System.out.println("aaaaa");
        System.out.println("encrypted"+encrypted);
        return encrypted;
    }

    @PostMapping(value = "/decrypt", produces = "text/html;charset=UTF-8")
    public String decrypt(String data, String sign, String aeskey) {
//        logger.info(REC_ENC_MSG, data, sign, aeskey);
//        String decrypted = encryptService.decrypt(data, sign, aeskey);
        String decrypted = "";
//        logger.info(BACK_TEXT_MSG, decrypted);
        return decrypted;
    }
}
