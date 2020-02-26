package com.tcl.jr.ebank;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class HttpClientUtil
{

  private File caFile;
  private File certFile;
  private String certPasswd;
  private String reqContent;
  private String resContent;
  private String method;
  private String errInfo;
  private int timeOut;
  private int responseCode;
  private String charset;
  private InputStream inputStream;

  public HttpClientUtil()
  {
    this.caFile = null;
    this.certFile = null;
    this.certPasswd = "";

    this.reqContent = "";
    this.resContent = "";
    this.method = "POST";
    this.errInfo = "";
    this.timeOut = 30;

    this.responseCode = 0;
    this.charset = "UTF-8";

    this.inputStream = null;
  }

  public String getCharset() {
    return this.charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public void setCertInfo(File certFile, String certPasswd)
  {
    this.certFile = certFile;
    this.certPasswd = certPasswd;
  }

  public void setCaInfo(File caFile)
  {
    this.caFile = caFile;
  }

  public void setReqContent(String reqContent)
  {
    this.reqContent = reqContent;
  }

  public String getResContent()
  {
    try
    {
      doResponse();
    } catch (IOException e) {
      this.errInfo = e.getMessage();
    }

    return this.resContent;
  }

  public void setMethod(String method)
  {
    this.method = method;
  }

  public String getErrInfo()
  {
    return this.errInfo;
  }

  public void setTimeOut(int timeOut)
  {
    this.timeOut = timeOut;
  }

  public int getResponseCode()
  {
    return this.responseCode;
  }

  public boolean call()
  {
    boolean isRet = false;

    if ((null == this.caFile) && (null == this.certFile)) {
      try {
        callHttp();
        isRet = true;
      } catch (IOException e) {
        this.errInfo = e.getMessage();
      }
      return isRet;
    }

    try
    {
      callHttps();
      isRet = true;
    } catch (UnrecoverableKeyException e) {
      this.errInfo = e.getMessage();
    } catch (KeyManagementException e) {
      this.errInfo = e.getMessage();
    } catch (CertificateException e) {
      this.errInfo = e.getMessage();
    } catch (KeyStoreException e) {
      this.errInfo = e.getMessage();
    } catch (NoSuchAlgorithmException e) {
      this.errInfo = e.getMessage();
    } catch (IOException e) {
      this.errInfo = e.getMessage();
    }

    return isRet;
  }

  protected void callHttp()
    throws IOException
  {
    if ("POST".equals(this.method.toUpperCase())) {
      String url = HttpClient.getURL(this.reqContent);
      String queryString = HttpClient.getQueryString(this.reqContent);
      byte[] postData = queryString.getBytes(this.charset);
      httpPostMethod(url, postData);

      return;
    }

    httpGetMethod(this.reqContent);
  }

  protected void callHttps()
    throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException
  {
    String caPath = this.caFile.getParent();

    File jksCAFile = new File(caPath + "/" + "nos_cacert.jks");

    if (!jksCAFile.isFile())
    {
      X509Certificate cert = (X509Certificate)HttpClient.getCertificate(this.caFile);

      FileOutputStream out = new FileOutputStream(jksCAFile);

      HttpClient.storeCACert(cert, "nos", "", out);

      out.close();
    }

    FileInputStream trustStream = new FileInputStream(jksCAFile);
    FileInputStream keyStream = new FileInputStream(this.certFile);

    SSLContext sslContext = HttpClient.getSSLContext(trustStream, "", keyStream, this.certPasswd);

    keyStream.close();
    trustStream.close();

    if ("POST".equals(this.method.toUpperCase())) {
      String url = HttpClient.getURL(this.reqContent);
      String queryString = HttpClient.getQueryString(this.reqContent);
      byte[] postData = queryString.getBytes(this.charset);

      httpsPostMethod(url, postData, sslContext);

      return;
    }

    httpsGetMethod(this.reqContent, sslContext);
  }

  public boolean callHttpPost(String url, String postdata)
  {
    boolean flag = false;
    try
    {
      byte[] postData = postdata.getBytes(this.charset);
      httpPostMethod(url, postData);
      flag = true;
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return flag;
  }

  protected void httpPostMethod(String url, byte[] postData)
    throws IOException
  {
    HttpURLConnection conn = HttpClient.getHttpURLConnection(url);

    doPost(conn, postData);
  }

  protected void httpGetMethod(String url)
    throws IOException
  {
    HttpURLConnection httpConnection = HttpClient.getHttpURLConnection(url);

    setHttpRequest(httpConnection);

    httpConnection.setRequestMethod("GET");

    this.responseCode = httpConnection.getResponseCode();

    this.inputStream = httpConnection.getInputStream();
  }

  protected void httpsGetMethod(String url, SSLContext sslContext)
    throws IOException
  {
    SSLSocketFactory sf = sslContext.getSocketFactory();

    HttpsURLConnection conn = HttpClient.getHttpsURLConnection(url);

    conn.setSSLSocketFactory(sf);

    doGet(conn);
  }

  protected void httpsPostMethod(String url, byte[] postData, SSLContext sslContext)
    throws IOException
  {
    SSLSocketFactory sf = sslContext.getSocketFactory();

    HttpsURLConnection conn = HttpClient.getHttpsURLConnection(url);

    conn.setSSLSocketFactory(sf);

    doPost(conn, postData);
  }

  protected void setHttpRequest(HttpURLConnection httpConnection)
  {
    httpConnection.setConnectTimeout(this.timeOut * 1000);

    httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)");

    httpConnection.setUseCaches(false);

    httpConnection.setDoInput(true);
    httpConnection.setDoOutput(true);
  }

  protected void doResponse()
    throws IOException
  {
    if (null == this.inputStream) {
      return;
    }

    this.resContent = HttpClient.InputStreamTOString(this.inputStream, this.charset);

    this.inputStream.close();
  }

  protected void doPost(HttpURLConnection conn, byte[] postData)
    throws IOException
  {
    conn.setRequestMethod(this.method);

    setHttpRequest(conn);

    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    BufferedOutputStream out = new BufferedOutputStream(conn
      .getOutputStream());

    HttpClient.doOutput(out, postData, 1024);

    out.close();

    this.responseCode = conn.getResponseCode();

    this.inputStream = conn.getInputStream();
  }

  protected void doGet(HttpURLConnection conn)
    throws IOException
  {
    conn.setRequestMethod("GET");

    setHttpRequest(conn);

    this.responseCode = conn.getResponseCode();

    this.inputStream = conn.getInputStream();
  }
}