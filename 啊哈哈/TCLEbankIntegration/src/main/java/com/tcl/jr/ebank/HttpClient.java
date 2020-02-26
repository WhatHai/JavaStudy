package com.tcl.jr.ebank;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpClient {
    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

    public static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        return httpURLConnection;
    }

    public static HttpsURLConnection getHttpsURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        return httpsURLConnection;
    }

    public static String getURL(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;
    }

    public static String getQueryString(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    public static Map<String, String> queryString2Map(String queryString) {
        if ((null == queryString) || ("".equals(queryString))) {
            return null;
        }

        Map<String,String> m = new HashMap<String,String>();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            putMapByPair(pair, m);
        }

        return m;
    }

    public static void putMapByPair(String pair, Map<String, String> m) {
        if ((null == pair) || ("".equals(pair))) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if (-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if ((null != k) && (!"".equals(k)))
                m.put(k, v);
        } else {
            m.put(pair, "");
        }
    }

    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    public static void doOutput(OutputStream out, byte[] data, int len) throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < data.length) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
                off += dataLen;
            } else {
                out.write(data, off, len);
                off += len;
                dataLen -= len;
            }

            out.flush();
        }
    }

    public static SSLContext getSSLContext(FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd) throws NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = KeyStore.getInstance("JKS");
        trustKeyStore.load(trustFileInputStream, str2CharArray(trustPasswd));

        tmf.init(trustKeyStore);

        char[] kp = str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    public static Certificate getCertificate(File cafile) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    public static char[] str2CharArray(String str) {
        if (null == str)
            return null;

        return str.toCharArray();
    }

    public static void storeCACert(Certificate cert, String alias, String password, OutputStream out)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        ks.store(out, str2CharArray(password));
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static byte[] InputStreamTOByte(InputStream in) throws IOException {
        int BUFFER_SIZE = 4096;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        data = null;
        byte[] outByte = outStream.toByteArray();
        outStream.close();

        return outByte;
    }

    public static String InputStreamTOString(InputStream in, String encoding) throws IOException {
        return new String(InputStreamTOByte(in), encoding);
    }
}