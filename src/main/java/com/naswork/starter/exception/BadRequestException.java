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
 * @author eyaomai
 *
 */
public class BadRequestException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = 8068333482609260880L;

  public BadRequestException(ErrorEnum status) {
    super(status);
  }

  public BadRequestException(ErrorEnum status, String message) {
    super(status, message);
  }

}
