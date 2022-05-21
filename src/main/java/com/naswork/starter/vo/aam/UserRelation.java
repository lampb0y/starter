package com.naswork.starter.vo.aam;

import com.baomidou.mybatisplus.annotation.TableName;
import com.naswork.starter.model.SequenceIdModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 外部系统和本系统的用户的绑定关系
 * @Author: ChanningXie
 * @Date: 2021/1/29 15:59
 */
@Data
@TableName("AAM_USER_RELATION")
public class UserRelation extends SequenceIdModel {

  @ApiModelProperty(value = "外部系统的用户标识，和本系统的用户是绑定关系，多sso映射用", example = "xxxxxx")
  private String externalSystemUserId;

  @ApiModelProperty(value = "映射本系统的用户标识，可关联aamUser表", example = "xxxxxxx")
  private String thisSystemUserId;

  @ApiModelProperty(value = "映射本系统的用户名称", example = "张三")
  private String thisSystemUserName;

}
