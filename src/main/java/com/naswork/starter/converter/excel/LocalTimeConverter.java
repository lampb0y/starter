package com.naswork.starter.converter.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * easyExcel默认的类型转换器无法处理 JDK8 的 LocalTime，需自行编写转换器
 *
 * @author ztw
 * @since 2021/2/5 15:09
 */
public class LocalTimeConverter implements Converter<LocalTime> {

  private String pattern = "HH:mm:ss";

  @Override
  public Class<LocalTime> supportJavaTypeKey() {
    return LocalTime.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public LocalTime convertToJavaData(
      CellData cellData, ExcelContentProperty property, GlobalConfiguration globalConfig) {
    return LocalTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(pattern));
  }

  @Override
  public CellData<String> convertToExcelData(
      LocalTime value, ExcelContentProperty property, GlobalConfiguration globalConfig) {
    return new CellData<>(value.format(DateTimeFormatter.ofPattern(pattern)));
  }
}
