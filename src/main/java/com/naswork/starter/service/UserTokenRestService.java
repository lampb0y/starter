package com.naswork.starter.service;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.config.SSOProperties;
import com.naswork.starter.exception.UnauthorizedException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import com.naswork.starter.config.sso.JwtAuthenticationToken;

/**
 * 提供基于用户token，微服务之间的rest请求。
 * 例如当微服务A需要访问微服务B所提供的rest接口，可通过在A微服务中，使用本service，
 * 通过用户的token去调用B的rest接口。
 * @author eyaomai
 *
 */
@Service
public class UserTokenRestService extends RestBaseService {
  @Autowired
  protected HttpServletRequest request;

  @Override
  protected String getTokenString() {
    JwtAuthenticationToken token = 
        (JwtAuthenticationToken) request.getUserPrincipal();
    this.logger.debug("user token:{}", token.getTokenStr());
    return token.getTokenStr();
  }

  @Override
  protected String getTokenString(SSOProperties ssoServerProperties) {
    return null;
  }
}
