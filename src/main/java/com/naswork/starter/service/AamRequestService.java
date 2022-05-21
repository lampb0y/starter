package com.naswork.starter.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naswork.starter.exception.BadRequestException;
import com.naswork.starter.exception.ServerException;
import com.naswork.starter.exception.ServiceException;
import com.naswork.starter.exception.UnauthorizedException;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.aam.GroupBasicInfoVO;
import com.naswork.starter.vo.aam.UserFullInfoVO;
import com.naswork.starter.vo.aam.UserGroupsFullInfoVO;
import com.naswork.starter.vo.aam.UserRelation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 调用aam
 */
@Component
public class AamRequestService extends BaseService {

  public String queryUserRelation(String userId) {
    String userInfoUrl = this.aamUrl + "/v1/portal/external-user/relation";
    try {
      Map<String, Object> param = new HashMap();
      if (userId != null) {
        param.put("userId", userId);
      }
      ResponseEntity<String> response = this.userTokenRestService.sendGet(userInfoUrl, param);
      if (response.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        if (null != response.getBody()) {
          UserRelation info = mapper.readValue(response.getBody(), UserRelation.class);
          return info.getThisSystemUserId();
        }
      }
    } catch (Exception e) {
      throw new ServerException(ErrorEnum.UNKNOWN_ERROR, e);
    }
    return userId;
  }

  public UserGroupsFullInfoVO queryUserGroupsInfo(String groupCategory){
    String userInfoUrl = this.aamUrl + "/v1/portal/user/groups/info";
    try {
      Map<String, Object> param = new HashMap<>();
      if (groupCategory != null) {
        param.put("groupCategory", groupCategory);
      }
      ResponseEntity<String> response = this.userTokenRestService.sendGet(userInfoUrl, param);
      if (response.getStatusCode().is2xxSuccessful()) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), UserGroupsFullInfoVO.class);
      }
    } catch (Exception e) {
      throw new ServerException(ErrorEnum.UNKNOWN_ERROR, e);
    }
    return null;
  }

}

