package com.naswork.starter.vo.aam;

import java.util.List;
import java.util.Map;

import com.naswork.starter.model.StringIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户详细信息VO，用于用户使用自己的token来查看
 * @author eyaomai
 *
 */
@ApiModel
public class UserFullInfoVO extends UserInfoVO {


  /**
   * 
   */
  private static final long serialVersionUID = -6623355289785626129L;

  @ApiModelProperty(value = "用户所属组列表")
  private List<GroupBasicInfoVO> groupInfo;
  
  @ApiModelProperty(value = "用户所拥有角色")
  private List<RoleBasicInfoVO> roleInfo;

  @ApiModelProperty(value = "用户所拥有权限资源")
  private List<String> resource;

  @ApiModelProperty(value = "用户当前所属机构")
  private GroupBasicInfoVO currentGroup;

  public List<GroupBasicInfoVO> getGroupInfo() {
    return groupInfo;
  }

  public void setGroupInfo(List<GroupBasicInfoVO> groupInfo) {
    this.groupInfo = groupInfo;
  }

  public List<RoleBasicInfoVO> getRoleInfo() {
    return roleInfo;
  }

  public void setRoleInfo(List<RoleBasicInfoVO> roleInfo) {
    this.roleInfo = roleInfo;
  }

  public List<String> getResource() {
    return resource;
  }

  public void setResource(List<String> resource) {
    this.resource = resource;
  }

  public GroupBasicInfoVO getCurrentGroup() {
    return currentGroup;
  }

  public void setCurrentGroup(GroupBasicInfoVO currentGroup) {
    this.currentGroup = currentGroup;
  }
}
