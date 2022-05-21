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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author eyaomai
 *
 */
@ApiModel
public class StringIdModel extends BaseModel implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -7237650440791333810L;

  @ApiModelProperty(value = "id", example = "asdf-asf-asdf", position = 1)
  @TableId()
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StringIdModel)) {
      return false;
    }
    StringIdModel that = (StringIdModel) o;
    return Objects.equals(getId(), that.getId());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
