/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.utils;

import java.util.EnumMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.naswork.starter.vo.ActorType;


/**
 * set up content of label in common log and audit log.
 * 
 * @author elngjhx
 */
public class LogLabelUtils {

  private static final String SERVICE_ACCOUNT_PREFIX = "service-account-";

  private static String userName = "Unknown";


  private LogLabelUtils() {
  }


  /**
   * get actor ip address.
   * 
   * @param request
   *          HttpServletRequest
   * @return return ip of operator.
   */
  public static String getActorIp(HttpServletRequest request) {
    return request.getRemoteAddr();
  }

  /**
   * get actor id.
   * 
   * @return id of actor
   */
  static String getActorId() {
    return getUserName();
  }

  /**
   * get actor type.
   * 
   * @return type of actor
   */
  static ActorType getActorType() {
    if (getUserName().startsWith(SERVICE_ACCOUNT_PREFIX)) {
      return ActorType.SERVICE;
    } else {
      return ActorType.HUMAN;
    }
  }

  /**
   * get user name.
   * 
   * @return user name of operator
   */
  public static String getUserName() {
    return userName;
  }

  /**
   * set user name.
   * 
   * @param name
   *          user name
   */
  public static void setUserName(String name) {
    if (name == null) {
      userName = "Unknown";
    } else {
      userName = name;
    }
  }

}
