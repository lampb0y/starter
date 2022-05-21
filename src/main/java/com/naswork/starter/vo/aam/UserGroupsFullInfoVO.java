package com.naswork.starter.vo.aam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 用户详细信息VO，用于用户使用自己的token来查看
 * @author eyaomai
 *
 */
@ApiModel
public class UserGroupsFullInfoVO extends UserFullInfoVO {


  /**
   * 
   */
  private static final long serialVersionUID = -6623355289785626129L;

  @ApiModelProperty(value = "用户所属组-子组id列表")
  private List<String> childGroupsIds;

  public List<String> getChildGroupsIds() {
    return childGroupsIds;
  }

  public void setChildGroupsIds(List<String> childGroupsIds) {
    this.childGroupsIds = childGroupsIds;
  }

}
