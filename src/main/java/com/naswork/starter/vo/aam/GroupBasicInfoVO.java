package com.naswork.starter.vo.aam;

import com.naswork.starter.model.StringIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 组基本信息VO，用于非管理员查看，只有少量信息，不包含子节点信息
 * @author eyaomai
 *
 */
@ApiModel
public class GroupBasicInfoVO extends StringIdModel {

  /**
   * 
   */
  private static final long serialVersionUID = 2073187968808124924L;

  public static final int GROUP_TYPE_UNKNOWN = 0;
  public static final int GROUP_TYPE_ORG = 1;
  public static final int GROUP_TYPE_DEP = 2;
  public static final int GROUP_TYPE_ENTERPRISE = 3;
  public static final int GROUP_TYPE_IMPLEMENTATION = 4;
  
  public static final int DEPARTMENT_TYPE_UNKNOWN = 0;
  public static final int DEPARTMENT_TYPE_ENV = 1;
  
  /**
   * 未知
   */
  public static final int ORG_LEVEL_UNKNOWN = 0;
  /**
   * 国家级
   */
  public static final int ORG_LEVEL_NATION = 1;
  /**
   * 省级
   */
  public static final int ORG_LEVEL_PROVINCE = 2;
  /**
   * 市级
   */
  public static final int ORG_LEVEL_CITY = 3;
  /**
   * 镇/区级
   */
  public static final int ORG_LEVEL_DISTRICT = 4;
  /**
   * 村/居委级
   */
  public static final int ORG_LEVEL_VILLIAGE = 5;
  
  @ApiModelProperty(value = "机构类型，1：机构，2：部门", example = "1")
  private int groupType;
  
  @ApiModelProperty(value = "组织类型（创建和更新时无需传入）："
      + "government，enterprise，implementation，……", 
      example = "1")
  private String category;
  
  @ApiModelProperty(value = "父组织id", example = "234dafd-234ad-3234")
  private String parentId;
  
  /**
   * 为方便前端树状结构展示，无需前端转换，将机构名称字段命名为title
   */
  @ApiModelProperty(value = "机构名称", example = "大气科")
  private String title;
  
  @ApiModelProperty(value = "详细路径（创建和更新时无需传入）", example = "/东莞市/虎门环保局/大气科")
  private String path;

  @ApiModelProperty(value = "机构级别，0：无，1：国家，2：省/自治区/直辖市，3：市/县，4：镇/区/乡，5：村/居委", example = "3")
  private int orgLevel;

  @ApiModelProperty(value = "组织编码", example = "ORG123")
  private String orgCode;
  
  @ApiModelProperty(value = "职能部门类型，如环保/公安等，0：未知，1：环保，2或者以上是其他", example = "ORG123")
  private int departmentType;

  @ApiModelProperty(value = "所属权限", example = "长安镇")
  private String belongToAuthority;

  @ApiModelProperty(value = "地址", example = "长安镇")
  private String address;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getGroupType() {
    return groupType;
  }

  public void setGroupType(int groupType) {
    this.groupType = groupType;
  }

  public int getOrgLevel() {
    return orgLevel;
  }

  public void setOrgLevel(int orgLevel) {
    this.orgLevel = orgLevel;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }

  public int getDepartmentType() {
    return departmentType;
  }

  public void setDepartmentType(int departmentType) {
    this.departmentType = departmentType;
  }

  public String getBelongToAuthority() {
    return belongToAuthority;
  }

  public void setBelongToAuthority(String belongToAuthority) {
    this.belongToAuthority = belongToAuthority;
  }
}
