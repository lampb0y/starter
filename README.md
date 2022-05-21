This is the starter for all spring boot projects which shall include as submodule

## 错误处理
starter定义了错误基类BaseException，以及以下几个扩展类：
* NotFoundException：返回404
* ServerException：内部错误，返回500
* ServiceException：服务类错误，返回400错误
* UnauthorizedException：授权认证错误，返回401错误
  
以后根据需要，还可定义更多的通用扩展类。

### ResponseExceptionHandler
出错处理类，根据exception的类型包装http code以及返回的json。返回的json具有以下格式：

```
{
    "code":"<app>-<codeid>",
    "message":"xxxx"
}
```

其中：
* app为应用的名称，从配置文件的spring.application.name获取
* codeid为内部code id，格式为<code>3位http code-2位内部代码</code>，例如<code>40001</code>

## 微服务间调用
后台微服务之间需要通讯，即存在服务A需要调用服务B的restful接口的需求。实现上可以通过两种方式进行调用：
1. client token方式：这种方式为推荐方式，即服务A以client credential方式去SSO服务器（keycloak）获取token，所获取到的token称为client token；然后以该client token向微服务B发出restful请求。该方式适用于以下场景：  
    - 微服务A的一些后台程序，在没有用户参与的情况下调用B的服务；例如A初始化的时候，需要向B获取某些信息；
    - 需要向用户隐藏访问B的细节，即不能（无需）为用户分配访问B的权限，即可访问B的资源，在此情况下，只需为微服务A所使用的client分配相关权限即可；例如“预警微服务”需要调用“短信发送微服务”来将预警发送给干系人，那么无需用户有“短信发送微服务”的相关权限，只需要为“预警微服务”所使用的client赋予相关权限即可
  
2. user token方式：这种方式是微服务A以用户访问A的http请求中所带的token，向微服务B发出restful请求。该方式适用于以下场景：  
    - 微服务A实际上作为用户的代理来访问B，主要是为了前端隐藏细节以及形成微服务间的事务（目前还不支持）

 注意：实际上对于user token的方式，除非对于事务性有强烈要求之外，我们还是建议通过前端调用，即将“前端”->“微服务A”->“微服务B”的调用关系，改为：“前端”->“微服务A”，“前端”->“微服务B”。  

### 配置与实现
基础框架提供了两个类分别对应两种访问方式：
- <code>ClientTokenRestService</code>
    * 所使用的client必须要在keycloak上将<code>Service Account Enable</code>属性设置为true
    * 初始化的时候会以client credential的方式向keycloak申请token，并根据token的过期时间，启动定时器定时向服务器刷新token
    * 配置上需要在application.yml文件配置<code>keycloak.bearer-only</code>和<code>keycloak.credentials.secret</code>两个参数。前者设置为true，后者设置为client的credential，例如：

```
        keycloak:  
          ...  
          credentials:  
            secret: "6fd15e05-4d14-4659-aec0-a4a427753981"  
          bearer-only: true
```


- <code>UserTokenRestService</code>
    * 使用上无需特殊配置

调用时，根据需要如下调用即可：

```java
@Autowired
ClientTokenRestService clientTokenRestService;

public void myfunc() {
  clientTokenRestService.sendGet(url, parameter);// url为微服务B的资源地址，如果是要发送post/put/delete，那么调用的接口分别是sendPost/sendPut/sendDelete
}
```
如果想使用user token方式访问微服务B的话，则注入UserTokenRestService即可，调用方式与ClientTokenRestService一致。

## Swagger引入与配置



### 导入依赖



1. 在starter服务的pom.xml的 <dependencies></dependencies>标签中加入：

   ```xml
   <!-- swagger start -->
   <dependency>
       <groupId>io.springfox</groupId>
       <artifactId>springfox-swagger2</artifactId>
       <version>${swagger.version}</version>
   </dependency>
   <dependency>
       <groupId>io.springfox</groupId>
       <artifactId>springfox-swagger-ui</artifactId>
       <version>${swagger.version}</version>
   </dependency>
   <!-- swagger end -->
   ```

2. 在starter服务的pom.xml的 <properties>  </properties>中加入：

   ```
   <swagger.version>2.9.2</swagger.version>

   ```

3. 在<repositories> </repositories>标签中加入：

   ```xml
   <!-- for Swagger2Markup -->
   <repository>
   	<snapshots>
           <enabled>true</enabled>
           <updatePolicy>always</updatePolicy>
   	</snapshots>
   	<id>jcenter-releases</id>
   	<name>jcenter</name>
   	<url>https://maven.aliyun.com/repository/public</url>
   </repository>

   ```

4. 在<plugins></plugins>标签中加入：

   ```xml
   <plugin>
       <groupId>org.asciidoctor</groupId>
       <artifactId>asciidoctor-maven-plugin</artifactId>
       <version>1.5.6</version>
       <configuration>
           <!--asciidoc文件目录-->
           <sourceDirectory>../docs/asciidoc</sourceDirectory>
           <!---生成html的路径-->
           <outputDirectory>../docs/html</outputDirectory>
           <backend>html</backend>
           <sourceHighlighter>coderay</sourceHighlighter>
           <attributes>
               <!--导航栏在左-->
               <toc>left</toc>
               <!--显示层级数-->
               <toclevels>3</toclevels>
               <!--自动打数字序号-->
               <sectnums>true</sectnums>
           </attributes>
       </configuration>
   </plugin>
   ```


### 基础配置

1. 在starter服务的config包下创建Swagger配置类SwaggerConfig：

   ```java
   package com.naswork.starter.config;

   import io.swagger.annotations.ApiOperation;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.annotation.Profile;
   import springfox.documentation.builders.ApiInfoBuilder;
   import springfox.documentation.builders.PathSelectors;
   import springfox.documentation.builders.RequestHandlerSelectors;
   import springfox.documentation.service.ApiInfo;
   import springfox.documentation.spi.DocumentationType;
   import springfox.documentation.spring.web.plugins.Docket;
   import springfox.documentation.swagger2.annotations.EnableSwagger2;

   @Configuration
   @Profile("dev") // 只在开发环境启用 Swagger
   @EnableSwagger2 // 注意该注解不要加在其他位置（如启动类）上
   public class SwaggerConfig {

       @Value("${spring.application.name}")
       private String appName;

       @Value("${rest.version}")
       private String version;

       @Bean
       public Docket createRestApi() {
           return new Docket(DocumentationType.SWAGGER_2)
                   .apiInfo(apiInfo())
                   .select()
                   .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                   .paths(PathSelectors.any())
                   .build();
       }

       private ApiInfo apiInfo() {
           return new ApiInfoBuilder()
                   .title(appName) // 使用服务名作为文档标题
                   .description(appName + " 服务的 API 文档")
                   .version(version) 
                   .build();
       }

   }
   ```

2. 生成离线文档代码：

   ```java
   public String generateAsciiDocsToFile() throws Exception {
       Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
               .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
               .withOutputLanguage(Language.ZH)
               .withPathsGroupedBy(GroupBy.TAGS)
               .withGeneratedExamples()
               .withoutInlineSchema()
               .build();

       String filePath = "./docs/asciidoc/" + appName + "接口文档";
       Swagger2MarkupConverter.from(new URL("http://localhost:" + serverPort + "/v2/api-docs"))
               .withConfig(config)
               .build()
               .toFile(Paths.get(filePath));
   }
   ```

