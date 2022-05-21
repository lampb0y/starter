package com.naswork.starter.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.exception.ServerException;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.aam.UserFullInfoVO;

/**
 * 服务类基类
 * @author eyaomai
 *
 */
public abstract class BaseService {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  protected UserTokenRestService userTokenRestService;

  @Value("${aam.url:}")
  protected String aamUrl;
  
  protected UserFullInfoVO queryUserInfo(String groupCategory) {
    String userInfoUrl = this.aamUrl + "/v1/portal/user/info";
    try {
      Map<String, Object> param = new HashMap();
      if (groupCategory != null) {
        param.put("groupCategory", groupCategory);
      }
      ResponseEntity<String> response = this.userTokenRestService.sendGet(userInfoUrl, param);
      if (response.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        UserFullInfoVO info = mapper.readValue(response.getBody(), UserFullInfoVO.class);
        return info;
      }
    } catch (Exception e) {
      throw new ServerException(ErrorEnum.UNKNOWN_ERROR, e);
    }
    return null;
  }
}
