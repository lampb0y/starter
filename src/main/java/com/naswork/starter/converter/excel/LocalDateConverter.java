package com.naswork.starter.converter.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * easyExcel默认的类型转换器无法处理 JDK8 的 LocalDate，需自行编写转换器
 *
 * @author ztw
 * @since 2021/2/5 15:08
 */
public class LocalDateConverter implements Converter<LocalDate> {

  private String pattern = "yyyy-MM-dd";

  @Override
  public Class<LocalDate> supportJavaTypeKey() {
    return LocalDate.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public LocalDate convertToJavaData(
      CellData cellData, ExcelContentProperty property, GlobalConfiguration globalConfig) {
    return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(pattern));
  }

  @Override
  public CellData<String> convertToExcelData(
      LocalDate value, ExcelContentProperty property, GlobalConfiguration globalConfig) {
    return new CellData<>(value.format(DateTimeFormatter.ofPattern(pattern)));
  }
}
