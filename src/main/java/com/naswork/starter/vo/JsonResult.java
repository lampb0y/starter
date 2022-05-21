/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.vo;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naswork.starter.config.GeneralProperties;
import com.naswork.starter.exception.BaseException;
import com.naswork.starter.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * handle error code.
 *
 * @author elngjhx
 *
 */
@ApiModel
public class JsonResult implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8120662484490401626L;
  private static Logger logger = LoggerFactory.getLogger(JsonResult.class);
  

  @ApiModelProperty(value = "错误代码",
      example = "demo-40001", position = 1)
  private String code;
  @ApiModelProperty(value = "错误详情", example = "错误: xxx", position = 2)
  private String message;

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  private String appendAppname(String code, String appname) {
    if (code == null) {
      return appname;
    } else if (code.startsWith(appname)) {
      return code;
    } else {
      return appname + "-" + code;
    }
    
  }
  
  private JsonResult(String code, String message) {
    GeneralProperties gp = BeanUtils.getBean(GeneralProperties.class);
    
    this.code = this.appendAppname(code, gp.getAppname());
    //append "." to the end of error message
    if (!message.endsWith(".")) {
      message += ".";
    }
    this.message = message;
  }

  public static JsonResult build(BaseException ex) {
    return new JsonResult(ex.getCode(), ex.getMessage());
  }
  public static JsonResult build(String code, String message){
    return new JsonResult(code, message);
  }

}
