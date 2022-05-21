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

import java.util.Map;

/**
 * util to save local variable.
 *
 */
public class ThreadLocalUtil {

  private ThreadLocalUtil() {}

  public static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

  /**
   * get a map in local thread.
   * @return a map which save local variable.
   */
  public static Map<String, Object> get() {
    return threadLocal.get();
  }

  /**
   * set local variable in local thread.
   * @param map entry of local variable set to local thread.
   */
  public static void set(Map<String, Object> map) {
    threadLocal.set(map);
  }

  /**
   * remove local variable in local thread to prevent memory leak.
   */
  public static void remove() {
    threadLocal.remove();
  }

}


