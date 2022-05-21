package com.naswork.starter.config.sso;

import com.naswork.starter.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ztw
 * @since 2021/3/9 17:00
 *
 * jwt 格式 toke 信息返回
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenModel extends BaseModel implements Serializable {

  private static final long serialVersionUID = 4516288256641620884L;

  private String accessToken; // token 内容
  private long expiresIn; // token 超时时间，单位（秒）

}
