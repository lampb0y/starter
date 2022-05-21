/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.contoller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.naswork.starter.exception.ServiceException;
import com.naswork.starter.model.StringIdModel;
import com.naswork.starter.service.AamRequestService;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.aam.UserGroupsFullInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.naswork.starter.config.sso.JwtAuthenticationToken;

import io.jsonwebtoken.Claims;
/**
 * 控制器基类所有自定义的restful控制器必须继承此基类
 * @author eyaomai
 *
 */
public abstract class BaseController {
  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  protected HttpServletRequest request;
  
  @Autowired
  protected AamRequestService aamRequestService;

  /**
   * 从请求中获取登录用户id。
   * 
   * @return
   */
  protected String getLoginUserId() {
    Principal p = request.getUserPrincipal();
    return p.getName();
  }

  /**
   * 从请求中获取登录用户id,并查找系统中的用户绑定关系，返回本系统id
   * 场景：移动单独登录到我们系统，请求头token是移动的，id也是移动的，
   * 使用此接口查询回绑定本系统的kc或其他的userId
   * 注意：如果没有绑定关系的话，返回的是原来的id
   * @return
   */
  protected String getRelationUserId() {
    String loginUserId = this.getLoginUserId();
    String thisSystemUserId = aamRequestService.queryUserRelation(loginUserId);
    return thisSystemUserId;
  }

  /**
   * 获取用户详细信息
   *
   * @return
   */
  protected UserGroupsFullInfoVO getUserGroupsInfo() {
    return aamRequestService.queryUserGroupsInfo(null);
  }

  protected String getUserGroupId() {
    UserGroupsFullInfoVO vo = aamRequestService.queryUserGroupsInfo(null);
    if (vo.getCurrentGroup() == null){
      throw new ServiceException(ErrorEnum.UNKNOWN_ERROR, "该用户未绑定机构，无法新增");
    }
    return vo.getCurrentGroup().getId();
  }

  public Claims getAcessToken() {
    JwtAuthenticationToken token = 
        (JwtAuthenticationToken) request.getUserPrincipal();
    return (Claims) token.getCredentials();
  }
  
  public String getAccessTokenString() {
    JwtAuthenticationToken token = 
        (JwtAuthenticationToken) request.getUserPrincipal();
    return token.getTokenStr();
  }
    
}
