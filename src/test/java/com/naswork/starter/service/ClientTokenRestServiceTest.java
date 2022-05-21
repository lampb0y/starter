package com.naswork.starter.service;

import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;

import com.naswork.starter.config.sso.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 *
 */
@SpringBootTest(classes = {ClientTokenRestService.class})
public class ClientTokenRestServiceTest {

  
  @Test
  public void testGetTokenStringWhenNullToken() {
    // The real object
    ClientTokenRestService service = new ClientTokenRestService();
    //spy based on real object
    ClientTokenRestService serviceSpy = Mockito.spy(service);
    try {
      serviceSpy.getTokenString();
    } catch (Exception e) {
      // ignore the exception
    }
    
    // Verify refreshToken function is called, the original
    // "private" shall be changed to package level
    Mockito.verify(serviceSpy).refreshToken();
  }
  
  @Test
  public void testGetTokenStringWhenNotNullTokenAndNotExpire() {
    // The real object
    ClientTokenRestService service = new ClientTokenRestService();
    //spy based on real object
    ClientTokenRestService serviceSpy = Mockito.spy(service);
    
    String accessToken = "random string";
    Claims claims = new DefaultClaims();
    Date now = new Date();
    Date later = new Date(now.getTime() + 10 * 1000);
    claims.setExpiration(later);
    service.token = new JwtAuthenticationToken(accessToken, claims, AuthorityUtils.NO_AUTHORITIES);
    serviceSpy.token = service.token;
    try {
      serviceSpy.getTokenString();
    } catch (Exception e) {
      // ignore the exception
    }
    
    // Verify refreshToken function is never called
    
    Mockito.verify(serviceSpy, Mockito.never()).refreshToken();
    
  }

  @Test
  public void testGetTokenStringWhenNotNullTokenAndExpire() {
    // The real object
    ClientTokenRestService service = new ClientTokenRestService();
    //spy based on real object
    ClientTokenRestService serviceSpy = Mockito.spy(service);
    
    String accessToken = "random string";
    Claims claims = new DefaultClaims();
    Date now = new Date();
    Date later = new Date(now.getTime() + 5 * 1000);
    claims.setExpiration(later);
    service.token = new JwtAuthenticationToken(accessToken, claims, AuthorityUtils.NO_AUTHORITIES);
    serviceSpy.token = service.token;
    try {
      serviceSpy.getTokenString();
    } catch (Exception e) {
      // ignore the exception
    }
    
    // Verify refreshToken function is called once
    
    Mockito.verify(serviceSpy).refreshToken();
    
  }
  
}
