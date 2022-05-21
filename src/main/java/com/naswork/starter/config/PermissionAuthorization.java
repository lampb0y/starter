/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.config;

import java.util.List;

/**
 * 基于权限授权的接口。所有需要基于权限授权的应用，均需要实现该接口并定义为一个bean
 * @author eyaomai
 *
 */
public interface PermissionAuthorization {

  /**
   * 判断给定的角色列表，是否有所指定的所有权限
   * @param roleList 角色列表
   * @param permissionList 权限列表
   * @return 如果permissionList的每一个权限，在roleList中所有角色拥有的权限集合中存在，则返回true，否则返回false
   */
  public boolean hasAllPermission(List<String> roleList, List<String> permissionList);
  
  /**
   * 判断给定的角色，是否存在指定权限列表中的一项
   * @param roleList
   * @param permissionList
   * @return roleList中所有角色拥有的权限集合中，存在permissionList中的任意一个权限，则返回true，否则返回false
   */
  public boolean hasAnyPermission(List<String> roleList, List<String> permissionList);
}
