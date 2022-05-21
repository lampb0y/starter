package com.naswork.starter.utils;

import org.junit.Assert;
import org.junit.Test;


/**
 * test {@link MdcUtils}.
 */
public class MdcUtilsTest {
  public static final String TRACE_ID_PATTERN
      = String.format("[0-9a-z]{%d}", MdcUtils.TRACE_ID_LENGTH);

  @Test
  public void testTraceId() {
    // remove when trace id doesn't exist
    MdcUtils.removeTraceId();
    MdcUtils.removeTraceId();
    Assert.assertNull(MdcUtils.getTraceId());

    // generate trace id
    MdcUtils.generateTraceId();
    String traceId1 = MdcUtils.getTraceId();
    Assert.assertTrue(traceId1.matches(TRACE_ID_PATTERN));

    // re-generate trace id
    MdcUtils.generateTraceId();
    String traceId2 = MdcUtils.getTraceId();
    Assert.assertTrue(traceId2.matches(TRACE_ID_PATTERN));
    Assert.assertNotEquals(traceId1, traceId2);

    // remove existing trace id
    MdcUtils.removeTraceId();
    Assert.assertNull(MdcUtils.getTraceId());
  }
}
