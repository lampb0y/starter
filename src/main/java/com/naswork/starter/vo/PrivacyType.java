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

/**
 * Indication of log record contains privacy information or not.<br>
 * the value should be: <br>
 * "PRIVATE": Indicates that privacy information is contained in the record <br>
 * "OPEN": Indicates that no privacy information is contained in the record
 */
public enum PrivacyType {
  PRIVATE, OPEN
}
