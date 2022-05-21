package com.naswork.starter.config.sso;

import com.naswork.starter.config.BuildJwt;
import io.jsonwebtoken.*;
import org.apache.commons.io.IOUtils;

import java.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
*ChanningXie
*@Date:2021/2/25 15:30
*/
public class BuildJWTUtils {



  /**
   * 读取资源文件
   *
   * @param fileName 文件的名称
   * @return
   */
  public static String readResourceKey(String fileName) {
    String key = null;
    try {
      InputStream inputStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
      assert inputStream != null;
      key = IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return key;
  }

  /**
   * 构建token
   * @param buildJwt 用户对象
   * @param ttlMillis 过期的时间-毫秒
   * @return
   * @throws Exception
   */
  public static String buildJwtRS256(BuildJwt buildJwt, long ttlMillis) throws Exception {

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
    // 读取私钥
//    String key = readResourceKey("static/rsa_private_key_pkcs8.pem");

    // 生成签名密钥
    // byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(buildJwt.getPrivateKey());
    Base64.Decoder decoder = Base64.getDecoder();
    byte[] keyBytes = decoder.decode(buildJwt.getPrivateKey());
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

    // 生成JWT的时间
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("name", buildJwt.getName());
    claims.put("type", "Bearer");

    // 生成jwt文件
    JwtBuilder builder = Jwts.builder()
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，
        // 一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
        .setClaims(claims)
        .setHeaderParam("typ", "JWT")
        .setIssuedAt(now) //iat = Issued At Time：jwt的签发时间
        .setIssuer(buildJwt.getIssuerUri())//iss = Issuer Identifier：必须。
        // 提供认证信息者的唯一标识。一般是一个https的url（不包含querystring和fragment部分）。
        .setAudience(buildJwt.getResource())//aud = Audience(s)：必须。标识ID
        // Token的受众。必须包含OAuth2的client_id。
        .setId(UUID.randomUUID().toString())//设置jti(JWT ID)：是JWT的唯一标识，
        // 根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
        .setSubject(buildJwt.getId()) //sub = Subject Identifier：代表这个JWT的主体，
        // 即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
        .signWith(privateKey); //设置签名使用的签名算法和签名使用的秘钥

    // 如果配置了过期时间
    if (ttlMillis >= 0) {
      // 当前时间加上过期的秒数
      long expMillis = nowMillis + ttlMillis;
      Date exp = new Date(expMillis);
      // 设置过期时间
      builder.setExpiration(exp);
      //exp = Expiration time：必须。过期时间，超过此时间的ID Token会作废不再被验证通过。
    }
    return builder.compact();
  }

  /**
   * 解密Jwt内容
   *
   * @param jwtString
   * @return
   */
  public static String parseJwtRS256(String jwtString) {
    Claims claims = null;
    try {
      // 读取公钥
      String key = readResourceKey("static/rsa_public_key.pem");
      // 生成签名公钥
      // byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
      Base64.Decoder decoder = Base64.getDecoder();
      byte[] keyBytes = decoder.decode(key);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicKey = keyFactory.generatePublic(keySpec);
      Jwt jwt = Jwts.parserBuilder().setSigningKey(publicKey).build().parse(jwtString);
      claims = (Claims) jwt.getBody();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return claims.get("uid", String.class);
  }

  public static void main(String[] args) throws Exception {
    BuildJwt buildJwt = new BuildJwt();
    String key = readResourceKey("static/rsa_private_key_pkcs8.pem");
    buildJwt.setId("a54439e7-6c7c-43a9-9024-1cf7958e6e49");
    buildJwt.setName("naswork2020");
    buildJwt.setIssuerUri("https://test.datanas.cn");
    buildJwt.setResource("123");
    buildJwt.setPrivateKey(key);
    long ttlMillis = 360000;
    String token = buildJwtRS256(buildJwt, ttlMillis);
//    parseJwtRS256(token);
    System.out.println(parseJwtRS256(token));
  }
}
