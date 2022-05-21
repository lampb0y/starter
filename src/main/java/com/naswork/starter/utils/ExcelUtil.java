package com.naswork.starter.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.naswork.starter.exception.ServerException;
import com.naswork.starter.vo.ErrorEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * excel 工具类
 *
 * @author ztw
 * @since 2021/2/5 10:25
 */
public class ExcelUtil {

  /**
   * 导出 excel，仅支持单工作表的导出，可以设置 工作表名称 和 自定义转换器列表
   * @param resp 请求相应对象
   * @param data 要导出的数据
   * @param clazz 要导出的数据的实体类 class
   * @param fileName 文件名
   * @param sheetName sheet 名称，可为 null
   * @param converterList 自定义转换器列表，可为 null 或空列表
   */
  public static void write(
      HttpServletResponse resp, List data, Class clazz, String fileName,
      String sheetName, List<Converter> converterList) {

    ExcelWriterBuilder writerBuilder = EasyExcel.write(getOutputStream(resp, fileName), clazz);

    if (converterList != null && converterList.size() > 0) {
      for (Converter converter: converterList) {
        writerBuilder.registerConverter(converter);
      }
    }

    writerBuilder.sheet(sheetName).doWrite(data);
  }

  /**
   * 导出 excel，仅支持单工作表的导出
   * @param resp 请求相应对象
   * @param data 要导出的数据
   * @param clazz 要导出的数据的实体类 class
   * @param fileName 文件名
   */
  public static void write(HttpServletResponse resp, List data, Class clazz, String fileName) {
    write(resp, data, clazz, fileName, null, null);
  }

  /**
   * 防止文件名中文乱码，设置 response 相关信息
   * @param resp 请求相应对象
   * @param fileName 文件名
   */
  private static OutputStream getOutputStream(HttpServletResponse resp, String fileName) {
    try {
      fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
      resp.setContentType("application/vnd.ms-excel");
      resp.setCharacterEncoding("utf-8");
      resp.setHeader(
          "Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
      resp.setHeader("Cache-Control", "no-store");
      return resp.getOutputStream();
    } catch (IOException e){
      throw new ServerException(ErrorEnum.UNKNOWN_ERROR, e);
    }
  }

}
