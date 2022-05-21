package com.naswork.starter.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 分页模型,其他查询相关的requestparam参数,可以继承该类来设置分页信息
 * @author eyaomai
 *
 */
@ApiModel
public class PageRequestModel extends BaseModel {

  @ApiModelProperty(value = "页数", example = "1")
  private int pageNum = 1;
  
  @ApiModelProperty(value = "每页大小", example = "10")
  private int pageSize = 10;
  
  @ApiModelProperty(value = "int参数", example = "10")
  private int moreInt = 1;
  
  @ApiModelProperty(value = "string参数", example = "hello")
  private String moreString;

  public int getMoreInt() {
    return moreInt;
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

  public void setMoreInt(int moreInt) {
    this.moreInt = moreInt;
  }

  public String getMoreString() {
    return moreString;
  }

  public void setMoreString(String moreString) {
    this.moreString = moreString;
  }
  
  
  
}
