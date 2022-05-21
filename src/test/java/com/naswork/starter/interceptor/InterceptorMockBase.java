package com.naswork.starter.interceptor;

import com.naswork.starter.config.GeneralProperties;
import com.naswork.starter.filter.RequestFilter;
import com.naswork.starter.utils.BeanUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * base config of interceptor.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@PrepareForTest(BeanUtils.class)
public abstract class InterceptorMockBase {

  MockMvc mvc;

  @Autowired
  WebApplicationContext context;

  ObjectMapper mapper = new ObjectMapper();

  @Before
  public void setupMockMvc() throws Exception {
    GeneralProperties gp = new GeneralProperties();
    gp.setAppname("testapp");
    PowerMockito.mockStatic(BeanUtils.class);
    
    PowerMockito
      .when(BeanUtils.getBean(GeneralProperties.class))
      .thenReturn(gp);
    mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(new RequestFilter()).build();
  }


}
