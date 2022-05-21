package com.naswork.starter.interceptor;

import javax.servlet.DispatcherType;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

/**
 * test {@linkplain KeycloakTokenErrorInterceptor}.
 */
@SpringBootTest(classes = {KeycloakTokenErrorInterceptor.class})
public class KeycloakTokenErrorInterceptorTest extends InterceptorMockBase {
  
  private static final String AUTH_HEADER = "WWW-Authenticate";

  private static final String AUTH_ERROR_PATTERN
      = "Bearer realm=\\\"(.+)\\\", error=\\\"(.+)\\\", error_description=\\\"(.+)\\\"";

  private static final String INVALID_AUTH_HEADER
      = "invalid authentication header";

  private static final String AUTH_HEADER_FAILED_TO_PARSE
      = "Bearer realm=\"testrealm\", error=\"invalid_token\","
          + " error_description=\"Failed to parse JWT\"";
  private static final String RESPONSE_CONTENT_FAILED_TO_PARSE
      = "{\"code\":\"testapp-40101\",\"message\":\"Unauthorized to perform the request."
          + " Failed to parse JWT.\"}";

  private static final String AUTH_HEADER_TOKEN_EXPIRED
      = "Bearer realm=\"testrealm\", error=\"invalid_token\","
          + " error_description=\"Token is not active\"";
  private static final String RESPONSE_CONTENT_TOKEN_EXPIRED
      = "{\"code\":\"testapp-40101\",\"message\":\"Unauthorized to perform the request."
          + " Token is not active.\"}";

  private static final String AUTH_HEADER_INVALID_ISSUER
      = "Bearer realm=\"testrealm\", error=\"invalid_token\","
          + " error_description=\"Invalid token issuer."
          + " Expected 'http://localhost:8080/auth/realms/testrealm',"
          + " but was 'http://localhost:8180/auth/realms/testrealm'\"";
  private static final String RESPONSE_CONTENT_INVALID_ISSUER
      = "{\"code\":\"testapp-40101\",\"message\":\"Unauthorized to perform the request."
          + " Invalid token issuer.\"}";

  private static final String RESPONSE_HEADER = "failed to verify keycloak token";

  /**
   * fail to parse token.
   */
  @Test
  public void failToParseToken() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.ERROR, AUTH_HEADER_FAILED_TO_PARSE,
        RESPONSE_CONTENT_FAILED_TO_PARSE);
  }

  /**
   * token expired.
   */
  @Test
  public void tokenExpired() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.ERROR, AUTH_HEADER_TOKEN_EXPIRED,
        RESPONSE_CONTENT_TOKEN_EXPIRED);
  }

  /**
   * issuer is invalid.
   */
  @Test
  public void invalidIssuer() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.ERROR, AUTH_HEADER_INVALID_ISSUER,
        RESPONSE_CONTENT_INVALID_ISSUER);
  }

  /**
   * dispatcher type of request is not error.
   */
  @Test
  public void dispatcherNotError() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.REQUEST, AUTH_HEADER_FAILED_TO_PARSE,
        RESPONSE_CONTENT_FAILED_TO_PARSE);
  }

  /**
   * authentication header in response is null.
   */
  @Test
  public void authHeaderIsNull() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.ERROR, null,
        null);
  }

  /**
   * authentication header in response not match the pattern of keycloak authentication header.
   */
  @Test
  public void authHeaderNotMatch() throws Exception {
    invokeInterceptorAndVerify(DispatcherType.ERROR, INVALID_AUTH_HEADER,
        null);
  }

  /**
   * invoke interceptor and verify the result.
   * 
   * @param dispatcherType
   *          dispatcher type of request
   * @param authHeader
   *          authentication header of response
   * @param responseContent
   *          response content to be verified
   */
  private void invokeInterceptorAndVerify(DispatcherType dispatcherType, String authHeader,
      String responseContent)
          throws Exception {

    MockServletContext context = new MockServletContext();
    MockHttpServletRequest request = new MockHttpServletRequest(context);
    request.setDispatcherType(dispatcherType);

    MockHttpServletResponse response = new MockHttpServletResponse();
    if (authHeader != null) {
      response.setHeader(AUTH_HEADER, authHeader);
    }

    // can be any value, just used to verify the values are changed by intercepter or not
    int originStatus = HttpStatus.NOT_FOUND.value();
    String originContentType = MediaType.APPLICATION_XML_VALUE;
    response.setStatus(originStatus);
    response.setContentType(originContentType);

    KeycloakTokenErrorInterceptor interceptor = new KeycloakTokenErrorInterceptor();
    boolean result = interceptor.preHandle(request, response, null);

    if (dispatcherType == DispatcherType.ERROR) {
      Assert.assertFalse(result);
    } else {
      Assert.assertTrue(result);
    }

    if (dispatcherType == DispatcherType.ERROR
        && authHeader != null && authHeader.matches(AUTH_ERROR_PATTERN)) {
      Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
      Assert.assertEquals(MediaType.APPLICATION_JSON_UTF8_VALUE, response.getContentType());
      Assert.assertEquals(RESPONSE_HEADER, response.getHeader(AUTH_HEADER));
      Assert.assertEquals(responseContent, response.getContentAsString());
    } else {
      Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
      Assert.assertEquals(MediaType.APPLICATION_XML_VALUE, response.getContentType());
      Assert.assertEquals(authHeader, response.getHeader(AUTH_HEADER));
      Assert.assertEquals("", response.getContentAsString());
    }
  }
}
