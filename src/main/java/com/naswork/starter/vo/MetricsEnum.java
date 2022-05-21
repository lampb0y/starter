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
import com.naswork.starter.aop.PrometheusMetricsAspect;

/**
 * Prometheus Name and Tag enum.
 *
 */
public enum MetricsEnum {
  METHOD_COST_TIME("method.cost.time", PrometheusMetricsAspect.DEFAULT_TAG_KEY),
  USER_COUNTER_SUCCESS("user.counter.success", PrometheusMetricsAspect.DEFAULT_TAG_KEY),
  USER_COUNTER_ERROR("user.counter.error", PrometheusMetricsAspect.DEFAULT_TAG_KEY),
  INTERNAL_ERROR_CAPTURING("error.categories", "errorCode");

  private String name;
  private String tag;

  MetricsEnum(String name, String tag) {
    this.name = name;
    this.tag = tag;
  }

  public String getName() {
    return name;
  }

  public String getTag() {
    return tag;
  }

}
