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
 * throw the exception if unauthorized unauthorized exception is a kind of exception than under
 * RuntimeException.
 *
 * @author elngjhx
 *
 */
public class UnauthorizedException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = -604958922035881008L;

  public UnauthorizedException(ErrorEnum status) {
    super(status);
  }

  public UnauthorizedException(String message) {
    super(ErrorEnum.UNAUTH_REQUEST, message);
  }
  
  public UnauthorizedException(ErrorEnum status, String message, Exception ex) {
    super(status, message, ex);
  }

}
