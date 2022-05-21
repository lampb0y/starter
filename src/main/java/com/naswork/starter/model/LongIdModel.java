/*
 * Copyright naswork 2020 - All Rights Reserved.
 * The copyright to the computer program(s) herein
 * is the property of naswork.The programs may
 * be used and/or copied only with written permission
 * from naswork or in accordance with the terms
 * and conditions stipulated in the agreement/contract
 * under which the program(s) have been supplied.
 */

package com.naswork.starter.model;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 用于类型为Long作为id的model
 * @author ztw
 *
 */
@ApiModel
public class LongIdModel extends BaseModel implements Serializable {

  private static final long serialVersionUID = 1204248040375630246L;

  @ApiModelProperty(value = "id", example = "1", position = 1)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LongIdModel)) return false;
    LongIdModel that = (LongIdModel) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
