package com.naswork.starter.vo.aam;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户信息
 * @author eyaomai
 *
 */
public class UserBasicInfoWithOrgCode extends UserBasicInfoVO {

  /**
   * 
   */
  private static final long serialVersionUID = -6389876562475234997L;

  @ApiModelProperty(value = "组织编码", example = "ORG123")
  private String orgCode;

  @ApiModelProperty(value = "用户登录名", example = "zhangsan")
  private String userName;

  public String getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  
  
}
