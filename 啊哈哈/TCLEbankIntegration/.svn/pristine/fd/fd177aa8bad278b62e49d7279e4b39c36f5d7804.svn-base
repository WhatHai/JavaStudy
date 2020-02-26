package com.tcl.jr.ebank.webService.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.jr.ebank.webService.util.EncryptionUtil;

@Service
public class EncryptionService {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionService.class);


    @Value("${keystore.path}")
    private String keystorePath;
    @Value("${keystore.password}")
    private String keystorePassword;
    @Value("${keystore.alias}")
    private String keystoreAlias;
    @Value("${cer.path}")
    private String cerPath;

    private static final String ERROR_MSG = "发生错误";


    public String encrypt(String text) {
        try {
            // 使用企业私钥对报文签名
            return EncryptionUtil.encryptData(text, keystorePath, keystorePassword, keystoreAlias, keystoreAlias, cerPath);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
            return null;
        }
    }

    public String decrypt(String data, String sign, String aesKey) {
        try {
            data=StringEscapeUtils.unescapeJava(data);
            sign=StringEscapeUtils.unescapeJava(sign);
            aesKey=StringEscapeUtils.unescapeJava(aesKey);
            return EncryptionUtil.decryptData(data, sign, aesKey, keystorePath, keystoreAlias, keystorePassword, cerPath);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
            return null;
        }
    }
}
