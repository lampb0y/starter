package com.naswork.starter.utils;

import java.util.UUID;

import org.slf4j.MDC;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
/**
 * Mdc辅助类，生成随机traceid
 * @author eyaomai
 *
 */
public class MdcUtils {
  public static final String TRACE_ID = "traceId";
  public static final int TRACE_ID_LENGTH = 17;

  private MdcUtils() {
  }

  /**
   * generate trace id and put the id into MDC.
   */
  public static void generateTraceId() {
    //String id = UUID.randomUUID().toString().replace("-", "").substring(0, TRACE_ID_LENGTH);
    String id = TraceContext.traceId();
    if (id == null || id.isEmpty() || id.equals("Ignored_Trace")) {
      id = UUID.randomUUID().toString().replace("-", "").substring(0, TRACE_ID_LENGTH);
    }
    MDC.put(TRACE_ID, id);
  }

  /**
   * remove trace id.
   */
  public static void removeTraceId() {
    MDC.remove(TRACE_ID);
  }

  /**
   * get trace id from MDC.
   * 
   * @return trace id
   *      
   */
  public static String getTraceId() {
    return MDC.get(TRACE_ID);
  }
}
