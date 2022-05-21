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

import com.naswork.starter.utils.MdcUtils;
import com.naswork.starter.vo.ErrorEnum;

/**
 * 服务器错误exception，建议准备返回50x的http error code的处理抛出此exception
 * 
 * @author eyaomai
 *
 */
public class ServerException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = 1496343820548942062L;

  public ServerException(ErrorEnum status) {
    super(status.setKeyWord(MdcUtils.getTraceId()));
  }

  public ServerException(ErrorEnum status, Exception ex) {
    super(status.setKeyWord(MdcUtils.getTraceId()), ex);
  }

  public ServerException(ErrorEnum status, String message) {
    super(status.setKeyWord(MdcUtils.getTraceId()), message);
  }
}
