/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.contoller;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.nio.file.Paths;

/**
 * 生成Swagger静态文档的相关接口
 */
@RestController
@RequestMapping("swagger")
public class SwaggerController {

  @Value("${spring.application.name}")
  private String appName;
  @Value("${server.port}")
  private String port;

  private String url = "http://localhost:" + port + "/v2/api-docs";

  /**
   * 生成AsciiDocs格式文档
   * @throws Exception
   */
  @GetMapping("adoc")
  public String generateAsciiDocsToFile() throws Exception {
    Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
        .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
        .withOutputLanguage(Language.ZH)
        .withPathsGroupedBy(GroupBy.TAGS)
        .withGeneratedExamples()
        .withoutInlineSchema()
        .build();

    String filePath = "./docs/asciidoc/" + appName + "接口文档";
    Swagger2MarkupConverter.from(new URL(url))
        .withConfig(config)
        .build()
        .toFile(Paths.get(filePath));
    return "200";
  }

}
