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
 * 服务类错误，该异常被框架捕获后，会返回40x错误
 * @author eyaomai
 *
 */
public class ServiceException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = 2867766552692290811L;

  public ServiceException(ErrorEnum status) {
    super(status);
  }
  
  public ServiceException(ErrorEnum status, String message) {
    super(status, message);
  }

}
