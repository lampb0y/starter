package com.naswork.starter.exception;

import com.naswork.starter.vo.ErrorEnum;

/**
 * @Author: D7-Dj
 * @Date: 2020/4/30 15:19
 */
public class NoPermissionException extends BaseException {
  private static final long serialVersionUID = 4324877256467433952L;
  
  public NoPermissionException(ErrorEnum status) {
    super(status);
  }
  
  public NoPermissionException(ErrorEnum status, Exception ex) {
    super(status, ex);
  }
}
