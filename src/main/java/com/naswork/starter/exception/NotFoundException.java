/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.exception;

import com.naswork.starter.vo.ErrorEnum;

/**
 * 找不到资源exception类，所有找不到资源的请求建议throw该类
 * @author eyaomai
 *
 */
public class NotFoundException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = -5883085454839184280L;

  public NotFoundException(ErrorEnum status, String message) {
    super(status, message);
  }

  public NotFoundException(ErrorEnum status) {
    super(status);
  }

  public NotFoundException(String message) {
    super(ErrorEnum.RESOURCE_NOT_FOUND, message);
  }
}
