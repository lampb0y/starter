package com.naswork.starter.vo.aam;

import java.util.List;

import com.naswork.starter.model.StringIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组基本信息VO，带有子节点信息
 * @author eyaomai
 *
 */
@ApiModel
public class GroupBasicInfoTreeVO extends GroupBasicInfoVO {

  /**
   * 
   */
  private static final long serialVersionUID = 2073187968808124924L;

  @ApiModelProperty(value = "子机构列表")
  private List<GroupBasicInfoTreeVO> children;
  
  public List<GroupBasicInfoTreeVO> getChildren() {
    return children;
  }

  public void setChildren(List<GroupBasicInfoTreeVO> children) {
    this.children = children;
  }

  public GroupBasicInfoVO copyWithoutChildren() {
    GroupBasicInfoVO vo = new GroupBasicInfoVO();
    vo.setCategory(this.getCategory());
    vo.setDepartmentType(this.getDepartmentType());
    vo.setGroupType(this.getGroupType());
    vo.setId(this.getId());
    vo.setOrgCode(this.getOrgCode());
    vo.setOrgLevel(this.getOrgLevel());
    vo.setParentId(this.getParentId());
    vo.setPath(this.getPath());
    vo.setTitle(this.getTitle());
    return vo;
  }
}
