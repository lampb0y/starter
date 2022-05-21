package com.naswork.starter.vo.aam;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naswork.starter.model.StringIdModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户详细信息VO，(除去groupinfo和roleinfo)
 * @author eyaomai
 *
 */
@ApiModel
public class UserInfoVO extends UserBasicInfoVO {

  /**
   * 
   */
  private static final long serialVersionUID = 6207634514838283384L;

  @ApiModelProperty(value = "用户登录名", example = "zhangsan")
  private String userName;
  
  @ApiModelProperty(value = "手机号码", example = "13812345678")
  private String mobile;
  
  @ApiModelProperty(value = "座机号码", example = "076934323212")
  private String landLine;

  @ApiModelProperty(value = "邮箱地址", example = "user@example.com")
  private String email;

  @ApiModelProperty(value = "是否属于本系统", example = "0否1是")
  private int belongSystem;
  
  @ApiModelProperty(value = "是否有效", example = "true")
  private boolean enable;


  @ApiModelProperty(value = "有效期开始时间", example = "2020-04-28 14:00:00", position = 10)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date startTime;

  @ApiModelProperty(value = "有效期结束时间", example = "2020-05-29 14:00:00", position = 15)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date endTime;

  @ApiModelProperty(value = "指挥调度用，其他项目可不使用，审核状态，1审核通过，" +
      "0/2待完善信息/待审核，3审核中，4审核不通过", example = "0否1是")
  private int auditStatus;

  public int getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(int auditStatus) {
    this.auditStatus = auditStatus;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getLandLine() {
    return landLine;
  }

  public void setLandLine(String landLine) {
    this.landLine = landLine;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public int getBelongSystem() {
    return belongSystem;
  }

  public void setBelongSystem(int belongSystem) {
    this.belongSystem = belongSystem;
  }
}
