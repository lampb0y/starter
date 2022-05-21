/*
* Copyright naswork 2019 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.filter.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * filter of console log from spring boot.
 * @author elngjhx
 */
public class CommonMarkerFilter extends Filter<Object> {

  @Override
  public FilterReply decide(Object eventObject) {
    LoggingEvent event = (LoggingEvent) eventObject;
    if (event.getMarker() == null) {
      return FilterReply.ACCEPT;
    }
    return FilterReply.DENY;
  }
}
