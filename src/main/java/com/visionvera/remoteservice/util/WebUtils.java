package com.visionvera.remoteservice.util;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * web工具类
 *
 * Created by EricShen on 2017/08/22
 */
public class WebUtils {

  private static PoolingHttpClientConnectionManager HTTP_CLIENT_CONNECTION_MANAGER = null;
  private static final CloseableHttpClient HTTP_CLIENT;
  private static final Log logger = LogFactory.getLog(WebUtils.class);

  public WebUtils() {
  }

  /**
   * 创建httpclient连接池并初始化
   */
  static {
    try {
      //添加对https的支持，该sslContext没有加载客户端证书
      // 如果需要加载客户端证书，请使用如下sslContext,其中KEYSTORE_FILE和KEYSTORE_PASSWORD分别是你的证书路径和证书密码
      //KeyStore keyStore  =  KeyStore.getInstance(KeyStore.getDefaultType()
      //FileInputStream instream =   new FileInputStream(new File(KEYSTORE_FILE));
      //keyStore.load(instream, KEYSTORE_PASSWORD.toCharArray());
      //SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,KEYSTORE_PASSWORD.toCharArray())
      // .loadTrustMaterial(null, new TrustSelfSignedStrategy())
      //.build();
      SSLContext sslContext = SSLContexts.custom()
          .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
          SSLConnectionSocketFactory.getDefaultHostnameVerifier());
      Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
          .register("https", sslsf)
          .register("http", PlainConnectionSocketFactory.getSocketFactory()).build();
      HTTP_CLIENT_CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(
          socketFactoryRegistry);
      HTTP_CLIENT_CONNECTION_MANAGER.setMaxTotal(50);
      HTTP_CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(25);
    } catch (Exception e) {
      logger.warn("httpUtils init get exception:");
    }
  }

  /**
   * 是否为ajax请求
   */
  public static boolean isAjax(HttpServletRequest request) {
    //如果是ajax请求响应头会有，x-requested-with
    return request.getHeader("x-requested-with") != null
        && request.getHeader("x-requested-with")
        .equalsIgnoreCase("XMLHttpRequest");
  }

  /**
   * 页面输出
   */
  public static void write(HttpServletResponse response, Object o) {
    try {
      response.setContentType("text/html;charset=utf-8");
      PrintWriter out = response.getWriter();
      out.println(o.toString());
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Map<String, String> parse(String query, String encoding) {
    Charset charset;
    if (org.apache.commons.lang3.StringUtils.isNotEmpty(encoding)) {
      charset = Charset.forName(encoding);
    } else {
      charset = Charset.forName("UTF-8");
    }

    List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(query, charset);
    Map<String, String> parameterMap = new HashMap();
    Iterator var5 = nameValuePairs.iterator();

    while (var5.hasNext()) {
      NameValuePair nameValuePair = (NameValuePair) var5.next();
      parameterMap.put(nameValuePair.getName(), nameValuePair.getValue());
    }

    return parameterMap;
  }

  public static Map<String, String> parse(String query) {
    return parse(query, (String) null);
  }

  public static String post(String url, Map<String, Object> parameterMap) {
    String result = null;
    CloseableHttpResponse httpResponse = null;

    try {
      List<NameValuePair> nameValuePairs = new ArrayList();
      if (parameterMap != null) {
        Iterator var5 = parameterMap.entrySet().iterator();

        while (var5.hasNext()) {
          Entry<String, Object> entry = (Entry) var5.next();
          String name = (String) entry.getKey();
          String value = String.valueOf(entry.getValue());
          if (org.apache.commons.lang3.StringUtils.isNotEmpty(name)) {
            nameValuePairs.add(new BasicNameValuePair(name, value));
          }
        }
      }

      HttpPost httpPost = new HttpPost(url);
      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
      httpResponse = HTTP_CLIENT.execute(httpPost);
      result = consumeResponse(httpResponse);
    } catch (UnsupportedEncodingException var15) {
      throw new RuntimeException(var15.getMessage(), var15);
    } catch (ClientProtocolException var16) {
      throw new RuntimeException(var16.getMessage(), var16);
    } catch (ParseException var17) {
      throw new RuntimeException(var17.getMessage(), var17);
    } catch (IOException var18) {
      throw new RuntimeException(var18.getMessage(), var18);
    } finally {
      close((CloseableHttpClient) null, httpResponse);
    }

    return result;
  }

  public static String post(String url, InputStreamEntity inputStreamEntity) {
    String result = null;
    CloseableHttpResponse httpResponse = null;
    boolean var16 = false;

    HttpPost httpPost;
    try {
      var16 = true;
      httpPost = new HttpPost(url);
      inputStreamEntity.setContentEncoding("ISO8859-1");
      httpPost.setEntity(inputStreamEntity);
      httpResponse = HTTP_CLIENT.execute(httpPost);
      result = consumeResponse(httpResponse);
      var16 = false;
    } catch (UnsupportedEncodingException var19) {
      throw new RuntimeException(var19.getMessage(), var19);
    } catch (ClientProtocolException var20) {
      throw new RuntimeException(var20.getMessage(), var20);
    } catch (ParseException var21) {
      throw new RuntimeException(var21.getMessage(), var21);
    } catch (IOException var22) {
      throw new RuntimeException(var22.getMessage(), var22);
    } finally {
      if (var16) {
        InputStream content = null;

        try {
          content = inputStreamEntity.getContent();
          if (content != null) {
            content.close();
          }
        } catch (Exception var17) {
          var17.printStackTrace();
        }

        close((CloseableHttpClient) null, httpResponse);
      }
    }

    httpPost = null;

    try {
      InputStream content = inputStreamEntity.getContent();
      if (content != null) {
        content.close();
      }
    } catch (Exception var18) {
      var18.printStackTrace();
    }

    close((CloseableHttpClient) null, httpResponse);
    return result;
  }

  public static String get(String url, Map<String, Object> parameterMap) {
    String result = null;
    CloseableHttpResponse httpResponse = null;

    try {
      List<NameValuePair> nameValuePairs = new ArrayList();
      if (parameterMap != null) {
        Iterator var5 = parameterMap.entrySet().iterator();

        while (var5.hasNext()) {
          Entry<String, Object> entry = (Entry) var5.next();
          String name = (String) entry.getKey();
          String value = String.valueOf(entry.getValue());
          if (org.apache.commons.lang3.StringUtils.isNotEmpty(name)) {
            nameValuePairs.add(new BasicNameValuePair(name, value));
          }
        }
      }

      HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils
          .toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
      httpResponse = HTTP_CLIENT.execute(httpGet);
      result = consumeResponse(httpResponse);
    } catch (IOException | ParseException var12) {
      throw new RuntimeException(var12.getMessage(), var12);
    } finally {
      close((CloseableHttpClient) null, httpResponse);
    }

    return result;
  }

  public static String get(String url) {
    CloseableHttpResponse httpResponse = null;

    String result;
    try {
      HttpGet httpGet = new HttpGet(url);
      httpResponse = HTTP_CLIENT.execute(httpGet);
      result = consumeResponse(httpResponse);
    } catch (IOException | ParseException var7) {
      throw new RuntimeException(var7.getMessage(), var7);
    } finally {
      close((CloseableHttpClient) null, httpResponse);
    }

    return result;
  }

  private static String consumeResponse(CloseableHttpResponse httpResponse) {
    String result = null;

    try {
      HttpEntity httpEntity = httpResponse.getEntity();
      if (httpEntity != null) {
        result = EntityUtils.toString(httpEntity, "UTF-8");
        EntityUtils.consume(httpEntity);
      }
    } catch (IOException var11) {
      var11.printStackTrace();
    } finally {
      try {
        httpResponse.close();
      } catch (IOException var10) {
        throw new RuntimeException(var10.getMessage(), var10);
      }
    }

    return result;
  }

  public static String httpPostWithJSON(String url, Object obj) {
    HttpPost httpPost = new HttpPost(url);
    CloseableHttpClient client = HttpClients.createDefault();
    String respContent = null;
    StringEntity entity = new StringEntity(JSON.toJSONString(obj), "utf-8");
    entity.setContentEncoding("UTF-8");
    entity.setContentType("application/json");
    httpPost.setEntity(entity);
    CloseableHttpResponse resp = null;

    try {
      resp = client.execute(httpPost);
      if (resp.getStatusLine().getStatusCode() == 200) {
        respContent = consumeResponse(resp);
      }
    } catch (Exception var11) {
      throw new RuntimeException(var11.getMessage(), var11);
    } finally {
      close(client, resp);
    }

    return respContent;
  }

  public static String httpPutWithJSON(String url, Object obj) {
    return httpPutWithJSON(url, obj, (Map) null);
  }

  public static String httpPutWithJSON(String url, Object obj, Map<String, String> headers) {
    HttpPut httpPut = new HttpPut(url);
    httpPut.setHeader("Content-type", "application/json");
    if (headers != null && headers.size() > 0) {
      Iterator var4 = headers.entrySet().iterator();

      while (var4.hasNext()) {
        Entry<String, String> entry = (Entry) var4.next();
        httpPut.setHeader((String) entry.getKey(), (String) entry.getValue());
      }
    }

    StringEntity stringEntity = new StringEntity(JSON.toJSONString(obj), "UTF-8");
    httpPut.setEntity(stringEntity);
    String respContent = null;
    CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
    CloseableHttpResponse httpResponse = null;

    try {
      httpResponse = closeableHttpClient.execute(httpPut);
      if (httpResponse.getStatusLine().getStatusCode() == 200) {
        respContent = consumeResponse(httpResponse);
      }
    } catch (Exception var12) {
      throw new RuntimeException(var12.getMessage(), var12);
    } finally {
      close(closeableHttpClient, httpResponse);
    }

    return respContent;
  }

  public static String httpDelete(String url) {
    return httpDelete(url, (Map) null);
  }

  public static String httpDelete(String url, Map<String, String> headers) {
    HttpDelete httpDelete = new HttpDelete(url);
    if (headers != null && headers.size() > 0) {
      Iterator var3 = headers.entrySet().iterator();

      while (var3.hasNext()) {
        Entry<String, String> entry = (Entry) var3.next();
        httpDelete.setHeader((String) entry.getKey(), (String) entry.getValue());
      }
    }

    CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
    String respContent = null;
    CloseableHttpResponse httpResponse = null;

    try {
      httpResponse = closeableHttpClient.execute(httpDelete);
      if (httpResponse.getStatusLine().getStatusCode() == 200) {
        respContent = consumeResponse(httpResponse);
      }
    } catch (IOException var10) {
      throw new RuntimeException(var10.getMessage(), var10);
    } finally {
      close(closeableHttpClient, httpResponse);
    }

    return respContent;
  }

  private static void close(CloseableHttpClient closeableHttpClient, CloseableHttpResponse resp) {
    try {
      if (resp != null) {
        resp.close();
      }

      if (closeableHttpClient != null) {
        closeableHttpClient.close();
      }

    } catch (IOException var3) {
      throw new RuntimeException(var3.getMessage(), var3);
    }
  }

  static {
    HTTP_CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(100);
    HTTP_CLIENT_CONNECTION_MANAGER.setMaxTotal(200);
    RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
        .setConnectTimeout(60000).setSocketTimeout(60000).build();
    HTTP_CLIENT = HttpClientBuilder.create().setConnectionManager(HTTP_CLIENT_CONNECTION_MANAGER)
        .setDefaultRequestConfig(requestConfig).build();
  }

}
