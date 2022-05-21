package com.naswork.starter.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限查询基类（查询对象必须为方法第一位）
 */
public class QueryBaseModel {

  @ApiModelProperty(hidden = true)
  private String sql = "";

  @ApiModelProperty(value = "页码")
  private int pageNum = 1;

  @ApiModelProperty(value = "分页大小")
  private int pageSize = 10;

  @ApiModelProperty(value = "开始时间")
  private String startTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
}
