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

import static net.logstash.logback.marker.Markers.append;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import net.logstash.logback.marker.LogstashMarker;

import com.naswork.starter.vo.ActorType;
import com.naswork.starter.vo.ErrorEnum;
import com.naswork.starter.vo.PrivacyType;

/**
 * utils to bulid up logstashMarker for common log and audit log.
 * 
 * @author elngjhx
 *
 */
public class LogBuilder {

  private static final String KEY_TYPE = "type";
  private static final String KEY_PRIVACY = "privacy";

  private static final String KEY_ACTOR_ID = "actorId";
  private static final String KEY_ACTOR_TYPE = "actorType";
  private static final String KEY_ACTOR_IP = "actorIP";
  private static final String KEY_SUBJECT_TYPE = "subjectType";
  
  private static final String KEY_ACTION_TYPE = "action";
  private static final String KEY_ACTION_RESULT = "actionResult";
  private static final String KEY_ACTION_TIME = "actionTime";
  private static final String KEY_TRACE_ID = "traceId";
  
  private static final String KEY_URI = "uri";

  private static final String VALUE_TYPE_AUDIT = "AUDIT";
  private static final String VALUE_TYPE_COMMON = "COMMON";
  private static final String VALUE_ACTION_RESULT_SUCCESS = "SUCCESS";
  private static final String VALUE_ACTION_RESULT_FAILURE = "FAILURE";
  private static final String VALUE_ACTION_RESULT_DENY = "DENY";



  private LogBuilder() {
  }

  /**
   * append logstashMarker with tag "type" and "privacy".
   * 
   * @param type
   *          Indicate the type of the log. <br>
   *          The value should be:<br>
   *          "AUDIT": audit logging record."COMMON": just common logging record.
   * 
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @return logstashMarker with tag "type" and "privacy"
   */
  private static LogstashMarker appendTypePrivacy(String type, PrivacyType privacy) {
    return append(KEY_TYPE, type).and(append(KEY_PRIVACY, privacy));
  }

  /**
   * append logstashMarker with tag "actorType" , "actorIp" and "actorId".
   *
   * @param request
   *          HttpServletRequest
   * @return logstashMarker with tag "actorType" , "actorIp" and "actorId"
   */
  private static LogstashMarker appendActor(HttpServletRequest request) {
    String actorId = LogLabelUtils.getActorId();
    ActorType actorType = LogLabelUtils.getActorType();
    String actorIp = LogLabelUtils.getActorIp(request);
    return append(KEY_ACTOR_ID, actorId).and(append(KEY_ACTOR_TYPE, actorType))
        .and(append(KEY_ACTOR_IP, actorIp));
  }

  /**
   * append logstashMarker with tag "subjectId" , and "subjectType".
   * 
   * @param subjectType
   *          Indicate the type of the action target.<br>
   *          including TOPIC and ACL.
   * @return logstashMarker with tag "subjectId" , and "subjectType"
   */
  private static LogstashMarker appendSubject(String subjectType) {
    return append(KEY_SUBJECT_TYPE, subjectType);
  }

  /**
   * append logstashMarker with tag "actionType" ,"actionTime", and "actionResult".
   * 
   * @param actionType
   *          Indicate the action.<br>
   *          The value can be: CREATE,LIST,GET,DELETE and UPDATE.
   * @param actionResult
   *          Indicate the result of the action. The value should be: SUCCESS,FAILURE or DENY.
   * 
   * @return logstashMarker with tag "actionType" ,"actionTime", and "actionResult"
   */
  private static LogstashMarker appendAction(String actionType, String actionResult) {
    long time = System.currentTimeMillis();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+0"));
    String actionTime = simpleDateFormat.format(new Date(time));
    return append(KEY_ACTION_TYPE, actionType).and(append(KEY_ACTION_TIME, actionTime))
        .and(append(KEY_ACTION_RESULT, actionResult));
  }

  /**
   * append logstashMarker with tag "meta".
   * 
   * @param meta
   *          indicate the additional information on demand.
   * @return logstashMarker with tag "meta"
   */
  public static LogstashMarker appendMeta(Map<String, String> meta) {
    return append("meta", meta);
  }

  /**
   * append logstashMarker with tag of audit log.
   * 
   * @param request
   *          HttpServletRequest
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @param actionResult
   *          Indicate the result of the action. The value should be: SUCCESS,FAILURE or DENY.
   * @return logstashMarker with tag of audit log.
   */
  private static LogstashMarker auditMarker(HttpServletRequest request, PrivacyType privacy,
      String subjectType, String actionResult) {
    String actionType = request.getMethod();
    return appendTypePrivacy(VALUE_TYPE_AUDIT, privacy).and(appendActor(request))
        .and(appendSubject(subjectType)).and(appendAction(actionType, actionResult));
  }

  // Audit Marker
  /**
   * append logstashMarker with tag of audit log when action result is success.
   * 
   * @param request
   *          HttpServletRequest
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @return logstashMarker with tag of audit log when action result is success.
   */
  public static LogstashMarker buildSuccessAuditMarker(HttpServletRequest request,
      PrivacyType privacy, String subjectType) {
    return auditMarker(request, privacy, subjectType, VALUE_ACTION_RESULT_SUCCESS);
  }

  public static LogstashMarker buildBeginAuditMarker(HttpServletRequest request,
      PrivacyType privacy, String subjectType) {
    LogstashMarker marker = auditMarker(request, privacy, subjectType, 
        VALUE_ACTION_RESULT_SUCCESS);
    String uri = request.getRequestURI();
    return marker.and(append(KEY_URI, uri));
  }
  /**
   * append logstashMarker with tag of audit log when action result is failure.
   * 
   * @param request
   *          HttpServletRequest
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @return logstashMarker with tag of audit log when action result is failure.
   */
  public static LogstashMarker buildFailureAuditMarker(HttpServletRequest request,
      PrivacyType privacy, String subjectType) {
    return auditMarker(request, privacy, subjectType, VALUE_ACTION_RESULT_FAILURE);
  }

  /**
   * append logstashMarker with tag of audit log when action result is deny.
   * 
   * @param request
   *          HttpServletRequest
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @param metaMap
   *          a map to indicate the additional information on demand.
   * @return logstashMarker with tag of audit log when action result is deny.
   */
  private static LogstashMarker buildDenyAuditMarker(HttpServletRequest request,
      PrivacyType privacy, String subjectType, Map<String, String> metaMap) {
    return auditMarker(request, privacy, subjectType, VALUE_ACTION_RESULT_DENY)
        .and(appendMeta(metaMap));
  }

  /**
   * append logstashMarker with tag of audit log when action result is deny with meta.
   * 
   * @param request
   *          HttpServletRequest
   * @param error
   *          Error type
   * @return logstashMarker with tag of audit log when action result is deny with meta.
   *
   */
  public static LogstashMarker builtDenyAuditLog(HttpServletRequest request, 
      String subjectType, ErrorEnum error) {
    String reason = error.code() + ":" + error.message();
    Map<String, String> map = new LinkedHashMap<>();
    map.put("reason", reason);
    return buildDenyAuditMarker(request, PrivacyType.PRIVATE, subjectType, map);
  }

  // common marker
  /**
   * append logstashMarker with tag of common log.
   * 
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @return logstashMarker with tag of common log
   */
  public static LogstashMarker buildCommonMarker(PrivacyType privacy) {
    return appendTypePrivacy(VALUE_TYPE_COMMON, privacy);
  }
  
  /**
   * append logstashMarker with tag of common log when initialization.
   * 
   * @param privacy
   *          Indication of log record contains privacy information or not.<br>
   *          the value should be: <br>
   *          "PRIVATE": Indicates that privacy information is contained in the record <br>
   *          "OPEN": Indicates that no privacy information is contained in the record
   * @return logstashMarker with tag of common log
   */
  public static LogstashMarker buildCommonMarkerWhenInit(PrivacyType privacy) {
    return appendTypePrivacy(VALUE_TYPE_COMMON, privacy)
        .and(append(KEY_TRACE_ID, "----initialization----"));
  }

}
