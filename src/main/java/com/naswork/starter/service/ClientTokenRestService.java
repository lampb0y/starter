package com.naswork.starter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.config.SSOProperties;
import com.naswork.starter.config.sso.JwtAuthenticationToken;
import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * 提供基于client credential所需的微服务之间的rest请求。
 * 例如当微服务A需要访问微服务B所提供的rest接口，可通过在A微服务中，使用本service，
 * 通过client的token去调用B的rest接口。
 * 使用时需对A进行相关配置：
 * 1. 所使用的client要在keycloak上启用“Service Account Enabled”
 * 2. application.yml中，keycloak.bearer-only要设置为true，
 *  keycloak.credentials.secret 要设置为client的密钥
 * @author eyaomai
 *
 */
@Service
public class ClientTokenRestService extends RestBaseService {
  
  @Autowired
  private SSOProperties ssoProperties;

//  @Autowired
//  private MultiSSOProperties multiSSOProperties;
  

  protected JwtAuthenticationToken token;
  
  /**
   * 初始化并定期更新token
   */
  void refreshToken() {
    try {
//      ssoProperties为null时，默认使用第一个multiSSOProperties.getServers()
//      if (null == ssoProperties.getTokenUrl() &&
//          null != ssoProperties.getServers().get(0).getTokenUrl()
//      ) {
//        System.out.println(ssoProperties.getServers().get(0));
//        BeanUtils.copyProperties(ssoProperties, ssoProperties.getServers().get(0));
//        ssoProperties.setTokenUrl(ssoProperties.getServers().get(0).getTokenUrl());
//        ssoProperties.setResource(ssoProperties.getServers().get(0).getResource());
//        ssoProperties.setCredential(ssoProperties.getServers().get(0).getCredential());
//        ssoProperties.setScope(ssoProperties.getServers().get(0).getScope());
//        ssoProperties.setPublicKey(ssoProperties.getServers().get(0).getPublicKey());
//      }


      HttpPost httpPost = new HttpPost(ssoProperties.getTokenUrl());
      ArrayList<BasicNameValuePair> list = new ArrayList<>();
      list.add(new BasicNameValuePair("grant_type", "client_credentials"));
      list.add(new BasicNameValuePair("client_id", ssoProperties.getResource()));
      list.add(new BasicNameValuePair("client_secret", ssoProperties.getCredential()));
      list.add(new BasicNameValuePair("scope",
          ssoProperties.getScope() == null ? " " : ssoProperties.getScope() ));
      CloseableHttpClient httpClient = HttpClients.createDefault();
      httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
      HttpResponse response = httpClient.execute(httpPost);
      if (response.getStatusLine().getStatusCode() == 200) {
        org.apache.http.HttpEntity entity = response.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> tokenResult = mapper.readValue(entity.getContent(), Map.class);
        String accessToken = (String) tokenResult.get("access_token");
        System.out.println(accessToken);
        Claims claims = JWTUtils.parseJwt(ssoProperties.getPublicKey(), accessToken);
        this.token = new JwtAuthenticationToken(accessToken, claims, AuthorityUtils.NO_AUTHORITIES);
      } else {
        this.logger.error("Fail to refresh token:{}", IOUtils.toString(
            response.getEntity().getContent()));
      }
    } catch (Exception e) {
      this.logger.error("Fail to refresh token:", e);
      throw new UnauthorizedException("token过期，更新失败");
    }
    logger.info("Token expires at: {}", ((Claims) token.getCredentials()).getExpiration());
  }

  @Override
  protected String getTokenString() {
    if (token == null) {
      this.refreshToken();
    }
    Claims claims = (Claims) token.getCredentials();
    //为了避免所有客户同时向keycloak发出refresh请求，故设置一个随机值，使之
    // 能在过期前的5-10秒内随机发出请求
    Random r = new Random();
    int delayRandomSecond = r.nextInt(5) + 5;
    Date expirDate = claims.getExpiration();

//    logger.info("Claims exp at: {}", (int) claims.get("exp"));
//    logger.info("expirDate exp at: {}", expirDate.getTime());

    if ((expirDate.getTime() - delayRandomSecond * 1000) < System.currentTimeMillis()){
      this.refreshToken();
    }
//    if (((int) claims.get("exp") - delayRandomSecond) * 1000 -
//        delayRandomSecond < System.currentTimeMillis()) {
//      this.refreshToken();
//    }
    return this.token.getTokenStr();
  }

  @Override
  protected String getTokenString(SSOProperties ssoServerProperties) {
    if (null != ssoServerProperties) {
      ssoProperties.setTokenUrl(ssoServerProperties.getTokenUrl());
      ssoProperties.setResource(ssoServerProperties.getResource());
      ssoProperties.setCredential(ssoServerProperties.getCredential());
      ssoProperties.setScope(ssoServerProperties.getScope());
      ssoProperties.setPublicKey(ssoServerProperties.getPublicKey());
    }
    if (token == null) {
      this.refreshToken();
    }
    Claims claims = (Claims) token.getCredentials();
    //为了避免所有客户同时向keycloak发出refresh请求，故设置一个随机值，使之
    // 能在过期前的5-10秒内随机发出请求
    Random r = new Random();
    int delayRandomSecond = r.nextInt(5) + 5;
    Date expirDate = claims.getExpiration();
    if ((expirDate.getTime() - delayRandomSecond * 1000) < System.currentTimeMillis()){
      this.refreshToken();
    }
    return this.token.getTokenStr();
  }

}
