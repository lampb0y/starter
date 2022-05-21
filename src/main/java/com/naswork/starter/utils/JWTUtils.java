package com.naswork.starter.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
// import org.apache.commons.codec.binary.Base64;
import java.util.Base64;

/**
 * JWT utils
 * @author eyaomai
 *
 */
public class JWTUtils {

  public static Claims parseJwt(String publicKeyStr, String jwtString)
      throws CertificateException {
    PublicKey publicKey = CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(publicKeyStr.getBytes()))
        .getPublicKey();

    Jwt jwt = Jwts.parserBuilder().setAllowedClockSkewSeconds(100).
        setSigningKey(publicKey).build().parse(jwtString);
    return (Claims) jwt.getBody();
  }

  public static Claims parseJwtFromPublicKey(String publicKeyStr, String jwtString) throws
      NoSuchAlgorithmException, InvalidKeySpecException {

    Base64.Decoder decoder = Base64.getDecoder();

    java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
            decoder.decode(publicKeyStr));
    java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);
    Jwt jwt = Jwts.parserBuilder().setAllowedClockSkewSeconds(100).
        setSigningKey(publicKey).build().parse(jwtString);

    return (Claims) jwt.getBody();

  }

  public static Claims parseJwtRS256(String publicKeyStr, String jwtString)
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      // 生成签名公钥
    publicKeyStr = publicKeyStr.replace("-----BEGIN CERTIFICATE-----\n" , "");
    publicKeyStr = publicKeyStr.replace("\n-----END CERTIFICATE-----" , "");
    byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
//      claims = Jwts.parserBuilder()
//          .setSigningKey(publicKey)
//          .parseClaimsJws(jwtString).getBody();
    Jwt jwt = Jwts.parserBuilder().setAllowedClockSkewSeconds(100).
        setSigningKey(publicKey).build().parse(jwtString);
    return (Claims) jwt.getBody();
  }



  public static void main(String[] args) throws CertificateException, InvalidKeySpecException,
      NoSuchAlgorithmException, IOException {
////    testExtractByCert();
  }
}
