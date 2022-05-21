package com.naswork.starter.utils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES工具类
 * @author eyaomai
 *
 */
public class AESUtils {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  public byte[] encrypt(String content, String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      kgen.init(256, new SecureRandom(password.getBytes()));
      SecretKey secretKey = kgen.generateKey();
      byte[] enCodeFormat = secretKey.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      byte[] byteContent = content.getBytes("utf-8");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] result = cipher.doFinal(byteContent);
      return result;
    } catch (NoSuchPaddingException e) {
      logger.error("Fail to encrypt", e);
    } catch (NoSuchAlgorithmException e) {
      logger.error("Fail to encrypt", e);
    } catch (UnsupportedEncodingException e) {
      logger.error("Fail to encrypt", e);
    } catch (InvalidKeyException e) {
      logger.error("Fail to encrypt", e);
    } catch (IllegalBlockSizeException e) {
      logger.error("Fail to encrypt", e);
    } catch (BadPaddingException e) {
      logger.error("Fail to encrypt", e);
    }
    return null;
  }
  
  /**
   * 解密AES加密过的字符串
   * 
   * @param content
   *            AES加密过过的内容
   * @param password
   *            加密时的密码
   * @return 明文
   */
  public byte[] decrypt(byte[] content, String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      kgen.init(256, new SecureRandom(password.getBytes()));
      SecretKey secretKey = kgen.generateKey();
      byte[] enCodeFormat = secretKey.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] result = cipher.doFinal(content);
      return result;
    } catch (NoSuchAlgorithmException e) {
      logger.error("Fail to decrypt", e);
    } catch (NoSuchPaddingException e) {
      logger.error("Fail to decrypt", e);
    } catch (InvalidKeyException e) {
      logger.error("Fail to decrypt", e);
    } catch (IllegalBlockSizeException e) {
      logger.error("Fail to decrypt", e);
    } catch (BadPaddingException e) {
      logger.error("Fail to decrypt", e);
    }
    return null;
  }
  
  public static void main(String[] args) throws Exception {
    AESUtils utils = new AESUtils();
    /**
    String content = "{\r\n" + 
        "    \"id\": \"ccc5a61f-4455-4fbe-aebd-531dc08034d4\",\r\n" + 
        "    \"displayName\": \"张三\",\r\n" + 
        "    \"userName\": \"zhangsan\",\r\n" + 
        "    \"orgCode\": \"机构代码abcdefg\"\r\n" + 
        "}";
    String password = "很长一串密码english和中文合并";
    System.out.println("需要加密的内容：" + content);
    byte[] encrypt = utils.encrypt(content, password);
    System.out.println("加密后的2进制密文：" + new String(encrypt));
    String hexStr = utils.parseByte2HexStr(encrypt);
    System.out.println("加密后的16进制密文:" + hexStr);
    byte[] byte2 = utils.parseHexStr2Byte(hexStr);
    System.out.println("加密后的2进制密文：" + new String(byte2));
    **/
    String hexStr = "79A41C044B8FFFB24CAA6D1D43585D1A53315195824E"
        + "660BA57EC7793794DF6E0C3193BC9027C3E27A0FAB57FFA21A568D"
        + "DA55888CD96838649A740C32F405D71DD1A837848A8DD6DC036D97"
        + "6E783660245C66576E43E41BFE6CEA8545FD1A7407EDEA3EB4903F"
        + "6A678C87355FE5FD650DAD84694792E7A41B51D5BC4D14C53E2ED4"
        + "234B89EDC8F45979C17CD7A7AFFEE700FBDAFA1F4186BA9F6B7F4F3631E4";
    byte[] byte2 = utils.parseHexStr2Byte(hexStr);
    byte[] decrypt = utils.decrypt(byte2, "I am a key");
    System.out.println("解密后的内容：" + new String(decrypt, "utf-8"));
  }


  public String parseByte2HexStr(byte buf[]) {  
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  } 
  
  /**将16进制转换为二进制 
   * @param hexStr 
   * @return 
   */  
  public byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1) {
      return null;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }
  
}
