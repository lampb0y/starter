package com.naswork.starter.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

//import com.naswork.starter.config.SSOServerProperties;
import com.naswork.starter.config.SSOProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
/**
 * 提供微服务之间，基于token认证的rest请求基类，可以基于client token也可基于user token进行
 * 认证，分别有相关子类来提供具体的token获取实现。
 * @author eyaomai
 *
 */
public abstract class RestBaseService extends BaseService {
  
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private RestTemplate rest = new RestTemplate();
  
  protected abstract String getTokenString();

  protected abstract String getTokenString(SSOProperties ssoServerProperties);
  
  String appendParamToUri(String url, Map<String, Object> param) {
    if (param != null && !param.isEmpty()) {
      StringBuilder uriBuffer = new StringBuilder(url).append("?");
      for (String key: param.keySet()) {
        uriBuffer.append(key).append("=").append(param.get(key)).append("&");
      }
      String uri = uriBuffer.toString();
      return uri.substring(0, uri.length() - 1);
    }
    return url;
  }
  
  /**
   * 发出REST GET请求
   * @param url url地址
   * @param param 查询参数的map
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendGet(String url, Map<String, Object> param) 
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, param);
    logger.debug("uri:{}", uri);
    RequestEntity requestEntity = RequestEntity
        .get(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString()).build();
    return rest.exchange(requestEntity, String.class);
  }

  /**
   * 发出REST GET请求,通过指定 sso server进行token获取
   * @param url url地址
   * @param param 查询参数的map
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendGet(String url,
                                        Map<String, Object> param,
                                        SSOProperties ssoServerProperties)
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, param);
    logger.debug("uri:{}", uri);
    RequestEntity requestEntity = RequestEntity
        .get(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString(ssoServerProperties)).build();
    return rest.exchange(requestEntity, String.class);
  }

  /**
   * 发出HTTP POST请求
   * @param url url地址
   * @param queryParam 查询参数的map
   * @param body 请求body
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendPost(String url, Map<String, Object> queryParam,
      Object body) throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.post(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString())
        .body(body);
    return rest.exchange(requestEntity, String.class);
  }

  /**
   * 发出HTTP POST请求,通过指定 sso server进行token获取
   * @param url url地址
   * @param queryParam 查询参数的map
   * @param body 请求body
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendPost(String url,
                                         Map<String, Object> queryParam,
                                         Object body, SSOProperties ssoServerProperties)
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.post(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString(ssoServerProperties))
        .body(body);
    return rest.exchange(requestEntity, String.class);
  }
  
  /**
   * 发出HTTP PUT请求
   * @param url url地址
   * @param queryParam 查询参数的map
   * @param body 请求body
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendPut(String url, Map<String, Object> queryParam,
      Object body) throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.put(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString())
        .body(body);
    return rest.exchange(requestEntity, String.class);
  }

  /**
   * 发出HTTP PUT请求
   * @param url url地址
   * @param queryParam 查询参数的map
   * @param body 请求body
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendPut(String url, Map<String, Object> queryParam,
                                        Object body,
                                        SSOProperties ssoServerProperties)
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.put(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString(ssoServerProperties))
        .body(body);
    return rest.exchange(requestEntity, String.class);
  }
  
  /**
   * 发出HTTP DELETE请求
   * @param url url地址
   * @param queryParam 查询参数的map
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendDelete(String url, Map<String, Object> queryParam) 
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.delete(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString()).build();
    return rest.exchange(requestEntity, String.class);
    
  }

  /**
   * 发出HTTP DELETE请求
   * @param url url地址
   * @param queryParam 查询参数的map
   * @return 请求结果，由应用去判断response的http code，内容转换等
   * @throws URISyntaxException
   */
  public ResponseEntity<String> sendDelete(String url,
                                           Map<String, Object> queryParam,
                                           SSOProperties ssoServerProperties)
      throws URISyntaxException {
    String uri = this.appendParamToUri(url, queryParam);
    RequestEntity requestEntity = RequestEntity.delete(new URI(uri))
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + this.getTokenString(ssoServerProperties)).build();
    return rest.exchange(requestEntity, String.class);

  }
  
}
