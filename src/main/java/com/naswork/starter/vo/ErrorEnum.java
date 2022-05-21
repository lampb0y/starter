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

/**
 * error status enum.
 */
public enum ErrorEnum {

  CREATE(null, null),
  // ERROR-400
  WRONG_ARGUMENTS("40001", "Wrong arguments"),
  RESOURCE_NOT_FOUND("40401", "Resource not found"),

  // ERROR-401
  /**
   * 40101 Unauthorized to perform the request. Only {role_list} are allowed to perform
   * this operation.
   *
   */
  UNAUTH_REQUEST("40101", "Unauthorized to perform the request"),
  
  // ERROR-403
  /*
  * 40301  this operation. but don't have enough permissions to  perform this operation.
   */
  NOT_ENOUGH_PERMISSION("40301", "You don't have enough permissions to  perform this operation."),

  // ERROR-405
  /**
   * Method Not Allowed
   */
  METHOD_NOT_ALLOWED("40501", "Method Not Allowed"),
  // ERROR-409
  /**
   * 40101  this record alreadu exists。
   * The same attributes exist。
   * eg: id, name, email。
   */
  CONFLICT("40901", "This record already exists"),
  // ERROR-500
  UNKNOWN_ERROR("50000", "Internal error: %s");


  /**
   * set value in message format.
   *
   * @param value
   *          argument in message
   * @return Error entity
   */
  public ErrorEnum setKeyWord(Object... value) {
    ErrorEnum.CREATE.setMessage(String.format(message, value));
    ErrorEnum.CREATE.setCode(code);
    return ErrorEnum.CREATE;
  }

  /**
   * append value after message .
   *
   * @param value
   *          argument in message
   * @return Error entity
   */
  public ErrorEnum append(String value) {
    ErrorEnum.CREATE.setMessage(message + ". " + value);
    ErrorEnum.CREATE.setCode(code);
    return ErrorEnum.CREATE;
  }
  
  public ErrorEnum customizedError(String code, String message) {
    ErrorEnum.CREATE.setMessage(message);
    ErrorEnum.CREATE.setCode(code);
    return ErrorEnum.CREATE;
  }

  private String code;
  private String message;

  private ErrorEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String code() {
    return this.code;
  }

  public String message() {
    return this.message;
  }

  private void setMessage(String message) {
    this.message = message;
  }

  private void setCode(String code) {
    this.code = code;
  }

}
