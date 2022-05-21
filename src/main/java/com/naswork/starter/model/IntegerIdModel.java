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

/** 用于类型为integer作为id的model
 * @author eyaomai
 *
 */
@ApiModel
public class IntegerIdModel extends BaseModel implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8119825333354909809L;
  /**
   *
   */

  @ApiModelProperty(value = "id", example = "1", position = 1)
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntegerIdModel)) {
      return false;
    }
    IntegerIdModel that = (IntegerIdModel) o;
    return getId() == that.getId();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
