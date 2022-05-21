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
 * Abstract exception.
 *
 *
 */
public abstract class BaseException extends RuntimeException {


  /**
   * 
   */
  private static final long serialVersionUID = 7453921993196640910L;
  private final String code;
  private final String message;

  /**
   * constructor of BaseException with param Error.
   * @param status Error
   */
  public BaseException(ErrorEnum status) {
    super(status.message());
    this.code = status.code();
    this.message = status.message();
  }

  /**
   * constructor of BaseException with param Error and message.
   * @param status Error 
   * @param message error message
   */
  public BaseException(ErrorEnum status, String message) {
    super(status.message() + ": " + message);
    this.code = status.code();
    this.message = status.message() + ": " + message;
  }

  /**
   * constructor of BaseException with param Error, message and internal exception.
   * @param status Error
   * @param message error message
   * @param ex internal exception
   */
  public BaseException(ErrorEnum status, String message, Exception ex) {
    super(status.message() + ": " + message, ex);
    this.code = status.code();
    this.message = status.message() + ": " + message;
  }
  
  /**
   * constructor of BaseException with param Error, message and exception.
   * @param status Error
   * @param ex exception
   */
  public BaseException(ErrorEnum status, Exception ex) {
    super(status.message(), ex);
    this.code = status.code();
    this.message = status.message();
  }

  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

}
