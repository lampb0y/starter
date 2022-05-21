package com.naswork.starter.config;

import static org.junit.Assert.assertEquals;

import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.filter.RequestFilter;
import com.naswork.starter.utils.BeanUtils;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.JsonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * test {@link AuthExceptionEntryPoint}.
 */
@RunWith(PowerMockRunner.class)
@SpringBootTest(classes = {AuthExceptionEntryPoint.class})
@AutoConfigureMockMvc
@WebAppConfiguration
@PrepareForTest(BeanUtils.class)
public class AuthExceptionEntryPointTest {


  ObjectMapper mapper = new ObjectMapper();

  @Before
  public void setup() {
    GeneralProperties gp = new GeneralProperties();
    gp.setAppname("testapp");
    PowerMockito.mockStatic(BeanUtils.class);
    
    PowerMockito
      .when(BeanUtils.getBean(GeneralProperties.class))
      .thenReturn(gp);
  }
  
  @Test
  public void testAuthExceptionEntryPoint() throws ServletException, IOException {
    AuthExceptionEntryPoint point = new AuthExceptionEntryPoint();

    MockServletContext context = new MockServletContext();
    MockHttpServletRequest mockRequest = new MockHttpServletRequest(context);
    mockRequest.setMethod(HttpMethod.POST.name());
    RequestContextListener listener = new RequestContextListener();
    listener.requestInitialized(new ServletRequestEvent(context, mockRequest));
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authException = new BadCredentialsException(
        "bad credentials exception");
    point.commence(request, response, authException);
    String responseStr = response.getContentAsString();
    String expectResponseStr = mapper
        .writeValueAsString(JsonResult.build(new UnauthorizedException(
            ErrorEnum.UNAUTH_REQUEST.append("bad credentials exception"))));

    assertEquals(expectResponseStr, responseStr);
  }
}
