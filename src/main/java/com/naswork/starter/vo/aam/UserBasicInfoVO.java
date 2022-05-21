package com.naswork.starter.vo.aam;

import com.naswork.starter.model.StringIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户基本信息VO，用于非管理员查看，只有id和显示名称
 * @author eyaomai
 *
 */
@ApiModel
public class UserBasicInfoVO extends StringIdModel {

  /**
   * 
   */
  private static final long serialVersionUID = -8075026812902463776L;
  @ApiModelProperty(value = "用户显示名称", example = "张三")
  private String displayName;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
}
