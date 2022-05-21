package com.naswork.starter.interceptor;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * test {@linkplain ErrorPagesInterceptor}.
 */
@SpringBootTest(classes = {ErrorPagesInterceptor.class})
public class ErrorPagesInterceptorTest extends InterceptorMockBase {

  /**
   * mock request with error uri for post request.
   * 
   */
  @Test
  public void mockErrorUri_post() throws Exception {
    String requestBody = "{}";
    mvc.perform(MockMvcRequestBuilders.post("/UNKNOWN").contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(mapper.writeValueAsString(requestBody))).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
  }

  /**
   * mock request with error uri for get request.
   * 
   */
  @Test
  public void mockErrorUri_get() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/UNKNOWN")).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
  }

  /**
   * mock request with error uri for put request.
   */
  @Test
  public void mockErrorUri_put() throws Exception {
    String requestBody = "{}";
    mvc.perform(MockMvcRequestBuilders.put("/UNKNOWN").contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(mapper.writeValueAsString(requestBody))).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
  }

  /**
   * mock request with error uri for delete request.
   * 
   */
  @Test
  public void mockErrorUri_delete() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/UNKNOWN")).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
  }

}
