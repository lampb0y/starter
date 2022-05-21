/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.exception.handler;



import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.naswork.starter.exception.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.JsonResult;

/**
 * exception handler to handle custom exception.
 *
 * @author elngjhx
 *
 */
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Value("${spring.application.name}")
  private String appname;

  /**
   * built JsonResult and print error logger.
   *
   * @param ex
   *          exception
   * @return ResponseEntity include error code and message
   */
  private ResponseEntity<Object> builtHttpState(BaseException ex, HttpStatus status) {
    logger.error(ex.getMessage(), ex);
    JsonResult result = JsonResult.build(ex);
    return new ResponseEntity<>(result, status);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  public ResponseEntity<Object> handleBadRequestException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodNotAllowedException.class)
  @ResponseBody
  public ResponseEntity<Object> handleMethodNotAllowedException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(ConflictException.class)
  @ResponseBody
  public ResponseEntity<Object> handleConflictException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.CONFLICT);
  }
  
  /**
   * ServiceException handler.
   *
   * @param ex
   *          ServiceException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler(ServiceException.class)
  @ResponseBody
  public ResponseEntity<Object> handleServiceException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.BAD_REQUEST);
  }

  /**
   * ServiceException handler.
   *
   * @param ex
   *          ServiceException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler(NotFoundException.class)
  @ResponseBody
  public ResponseEntity<Object> handleNotFoundException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.NOT_FOUND);
  }
  /**
   * UnauthorizedException handler.
   *
   * @param ex
   *          UnauthorizedException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler(UnauthorizedException.class)
  @ResponseBody
  public ResponseEntity<Object> handleUnauthorizedException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.UNAUTHORIZED);
  }
  
  /**
   * NoPermissionException handler.
   *
   * @param ex
   *          NoPermissionException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler({NoPermissionException.class, AccessDeniedException.class})
  @ResponseBody
  public ResponseEntity<Object> handleNoPermissionException(RuntimeException ex) {
    NoPermissionException noPermissionException =
        new NoPermissionException(ErrorEnum.NOT_ENOUGH_PERMISSION, ex);
    return builtHttpState(noPermissionException, HttpStatus.FORBIDDEN);
  }

  /**
   * ServerException handler.
   *
   * @param ex
   *          ServerException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler({ServerException.class, BaseException.class})
  @ResponseBody
  public ResponseEntity<Object> handleServerException(BaseException ex) {
    return builtHttpState(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * AuthenticationException and TopicAuthorizationException handler.
   *
   * @param ex
   *          AuthenticationException or TopicAuthorizationException
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler({ AuthenticationException.class})
  @ResponseBody
  public ResponseEntity<Object> handleAuthenticationException(RuntimeException ex) {
    UnauthorizedException exception = new UnauthorizedException(
        ErrorEnum.UNAUTH_REQUEST, ex.getMessage(), ex);
    return builtHttpState(exception, HttpStatus.UNAUTHORIZED);
  }

  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    BadRequestException exception = null;
    if (ex.getCause() instanceof MismatchedInputException) {
      MismatchedInputException detailException = (MismatchedInputException) ex.getCause();
      // e.g.: com.naswork.workflow.lean.model.LeanTaskModel["taskType"]
      String ref = detailException.getPathReference();
      int leftIndex = ref.indexOf("\"");
      int rightIndex = ref.lastIndexOf("\"");
      String message = ref.substring(leftIndex + 1, rightIndex) + ":"
          + detailException.getOriginalMessage();
      exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS,
          message);
    } else {
      exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS,
          ex.getMessage());
    }
    return builtHttpState(exception, HttpStatus.BAD_REQUEST);
  }
  

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String message = "非法body参数：" + String.join(";",
        ex.getBindingResult().getFieldErrors().stream().map(
            item -> item.getField() + ":" + item.getDefaultMessage()
            ).collect(Collectors.toList()));
    BadRequestException exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS, message);
    return builtHttpState(exception, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String message = String.format("路径参数%s缺失", ex.getVariableName());
    BadRequestException exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS, message);
    return builtHttpState(exception, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String message = String.format("请求参数%s缺失", ex.getParameterName());
    BadRequestException exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS, message);
    return builtHttpState(exception, HttpStatus.BAD_REQUEST);
  }
  
  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String paramName = ex.getPropertyName();
    if (paramName == null &&
        ex instanceof MethodArgumentTypeMismatchException){
      paramName = ((MethodArgumentTypeMismatchException) ex).getName();
    }
    String message = String.format(
        "请求参数%s类型不正确,实际值为[%s],应为%s",
        paramName,
        ex.getValue(),
        ex.getRequiredType().getName());
    BadRequestException exception = new BadRequestException(ErrorEnum.WRONG_ARGUMENTS, message);
    return builtHttpState(exception, HttpStatus.BAD_REQUEST);
  }
  
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String message = String.format(
        "请求方法错误[%s]。",
        ex.getMethod());
    BadRequestException exception = new BadRequestException(
        ErrorEnum.METHOD_NOT_ALLOWED, message);
    return builtHttpState(exception, HttpStatus.METHOD_NOT_ALLOWED);
  }
  
  /**
   * other unexpected Exception handler.
   *
   * @param ex
   *          Exception
   * @return ResponseEntity include error code and message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleOtherError(Exception ex) {
    ServerException exception = new ServerException(ErrorEnum.UNKNOWN_ERROR, ex);
    return builtHttpState(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
}
