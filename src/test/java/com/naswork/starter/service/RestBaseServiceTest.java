package com.naswork.starter.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;

import com.naswork.starter.config.SSOProperties;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
@SpringBootTest(classes = {RestBaseService.class})
public class RestBaseServiceTest {

  @Test
  public void testAppendParamToUriWithNullPara() {
    ConcretClass c = new ConcretClass();
    String url = "http://a.b.c.d";
    String ret = c.appendParamToUri(url, null);
    Assert.assertEquals(url, ret);
    Map<String, Object> emptyParam = new HashMap();
    String ret2 = c.appendParamToUri(url, emptyParam);
    Assert.assertEquals(url, ret2);
  }
  
  
  @Test
  public void testAppendParamToUriWithOnePara() {
    ConcretClass c = new ConcretClass();
    String url = "http://a.b.c.d";
    Map<String, Object> param = new HashMap();
    param.put("key1", "value1");
    String ret = c.appendParamToUri(url, param);
    Assert.assertEquals(url + "?key1=value1", ret);
    
  }

  @Test
  public void testAppendParamToUriWithTwoPara() {
    ConcretClass c = new ConcretClass();
    String url = "http://a.b.c.d";
    Map<String, Object> param = new HashMap();
    param.put("key1", "value1");
    param.put("key2", "value2");
    String ret = c.appendParamToUri(url, param);
    try {
      Assert.assertEquals(url + "?key1=value1&key2=value2", ret);
    } catch (Exception e) {
      Assert.assertEquals(url + "?key2=value2&key1=value1", ret);
    }
    
  }
  
}

class ConcretClass extends RestBaseService {

  @Override
  protected String getTokenString() {
    // TODO(Auto-generated method stub)
    return null;
  }

  @Override
  protected String getTokenString(SSOProperties ssoServerProperties) {
    // TODO(Auto-generated method stub)
    return null;
  }
  
}
