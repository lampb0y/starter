package com.naswork.starter.vo.aam;

import com.naswork.starter.model.IntegerIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色基本信息VO，用于非管理员查看，只有id和显示名称
 * @author eyaomai
 *
 */
@ApiModel
public class RoleBasicInfoVO extends IntegerIdModel {

  /**
   * 
   */
  private static final long serialVersionUID = 2073187968808124924L;

  @ApiModelProperty(value = "角色名称", example = "管理员")
  private String roleName;
  
  @ApiModelProperty(value = "所属层级名称", example = "/东莞市/虎门环保局/大气科")
  private String hierachyName;

  @ApiModelProperty(value = "角色类型，0：系统自带，1：手动添加", example = "1")
  private int roleType;

  public int getRoleType() {
    return roleType;
  }

  public void setRoleType(int roleType) {
    this.roleType = roleType;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getHierachyName() {
    return hierachyName;
  }

  public void setHierachyName(String hierachyName) {
    this.hierachyName = hierachyName;
  }

}
