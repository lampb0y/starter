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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * wrap http servlet request to implement the reuse of input stream and reader.
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper {

  private final byte[] body;

  static byte[] getRequestBody(HttpServletRequest request) throws IOException {
    InputStream inputStream = request.getInputStream();
    byte[] buffer = new byte[1024];
    int len = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = inputStream.read(buffer)) != -1) {
      bos.write(buffer, 0, len);
    }
    bos.close();
    return bos.toByteArray();
  }
  
  public RequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    body = getRequestBody(request);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    final ByteArrayInputStream bais = new ByteArrayInputStream(body);
    return new ServletInputStream() {

      @Override
      public int read() throws IOException {
        return bais.read();
      }

      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        // no need to set read listener
      }

    };
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(
        new InputStreamReader(getInputStream(), StandardCharsets.UTF_8.name()));
  }

}
