/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/

package com.naswork.starter.config;


import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * 配置keycloak相关项
 * 
 *
 */
@Configuration
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class,
      excludeFilters = { @ComponentScan.Filter(type = FilterType.REGEX, 
          pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager") })
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "sso", value = "type",
      havingValue = "keycloak", matchIfMissing = true)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
    implements ApplicationContextAware, ApplicationEventPublisherAware, MessageSourceAware {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private SecurityProperties securityProperties;

  private ApplicationContext applicationContext;
  private ApplicationEventPublisher eventPublisher;
  private MessageSource messageSource;

  public SecurityConfig() {
    this.logger.info("init SecurityConfigBean");
  }
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    super.setApplicationContext(applicationContext);
    this.applicationContext = applicationContext;
  }

  @Override
  public void setApplicationEventPublisher(
      ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Registers the KeycloakAuthenticationProvider with the authentication manager.
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    logger.info("Register global");
    KeycloakAuthenticationProvider keycloakAuthenticationProvider
        = keycloakAuthenticationProvider();
    SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
    mapper.setPrefix("");
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(mapper);
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }

  /**
   * create keycloak authentication processing filter.
   * 
   * @return KeycloakAuthenticationProcessingFilter
   * 
   * @throws Exception in case of errors.
   */
  public KeycloakAuthenticationProcessingFilter customizedAuthenticationProcessingFilter()
      throws Exception {
    KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(
        authenticationManagerBean());
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
    filter.setApplicationContext(applicationContext);
    filter.afterPropertiesSet();
    filter.setMessageSource(messageSource);
    filter.setApplicationEventPublisher(eventPublisher);
    return generateKeycloakAuthenticationProcessingFilterProxy(filter);
  }

  /**
   * register authentication strategy.
   */
  @Override
  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }

  /**
   * register keycloak pre-auth action filter.
   * 
   * @param filter KeycloakPreAuthActionsFilter
   * @return FilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean<KeycloakPreAuthActionsFilter>
      keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter) {

    FilterRegistrationBean<KeycloakPreAuthActionsFilter> registrationBean
        = new FilterRegistrationBean<>(filter);

    registrationBean.setEnabled(false);

    return registrationBean;
  }

  /**
   * configure HTTP security.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //logger.info("ApiMatcher:{}", securityProperties.getApiMatcher());
    if (securityProperties.getWhiteList() != null && securityProperties.getWhiteList().size() > 0) {
      String[] whiteList = new String[securityProperties.getWhiteList().size()];
      securityProperties.getWhiteList().toArray(whiteList);
      http.cors()
      .configurationSource(corsConfigurationSource())
      .and()
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .authorizeRequests().antMatchers(whiteList).permitAll()
      .and()
      .addFilterAfter(customizedAuthenticationProcessingFilter(),
          BasicAuthenticationFilter.class).exceptionHandling()
      .authenticationEntryPoint(new AuthExceptionEntryPoint()).and()
      .authorizeRequests().antMatchers(securityProperties.getApiMatcher())
      .authenticated().anyRequest().permitAll();
      
    } else {
      http.cors()
      .configurationSource(corsConfigurationSource())
      .and()
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterAfter(customizedAuthenticationProcessingFilter(),
          BasicAuthenticationFilter.class).exceptionHandling()
      .authenticationEntryPoint(new AuthExceptionEntryPoint()).and()
      .authorizeRequests().antMatchers(securityProperties.getApiMatcher())
      .authenticated().anyRequest().permitAll();
    }
  }

  /**
   * register CorsConfigurationSource.
   * 
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    if (null != securityProperties.getCorsConfiguration()) {
      source.registerCorsConfiguration(securityProperties.getApiMatcher(),
          securityProperties.getCorsConfiguration());
    }
    return source;
  }

  /**
   * create filter proxy.
   * 
   * @param filter KeycloakAuthenticationProcessingFilter
   * 
   * @return KeycloakAuthenticationProcessingFilter
   */
  private KeycloakAuthenticationProcessingFilter
      generateKeycloakAuthenticationProcessingFilterProxy(KeycloakAuthenticationProcessingFilter
          filter) {

    ProxyFactory factory = new ProxyFactory();
    factory.setInterfaces(KeycloakAuthenticationProcessingFilter.class
        .getInterfaces());
    factory.addAdvice(new KeycloakAuthenticationProcessingFilterInterceptor());
    factory.setTarget(filter);
    factory.setOptimize(true);

    return (KeycloakAuthenticationProcessingFilter) factory.getProxy();
  }

  /**
   * intercept the method doFilter.
   */
  private class KeycloakAuthenticationProcessingFilterInterceptor implements
      MethodInterceptor {

    private static final String METHOD_NAME = "doFilter";

    private final AntPathRequestMatcher requestMatcher =
        new AntPathRequestMatcher(securityProperties.getApiMatcher());

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      if (METHOD_NAME.equals(invocation.getMethod().getName())) {
        HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
        HttpServletResponse response = (HttpServletResponse) invocation.getArguments()[1];
        FilterChain chain = (FilterChain) invocation.getArguments()[2];

        if (request.getDispatcherType() != DispatcherType.REQUEST
            || !requestMatcher.matches(request)) {
          chain.doFilter(request, response);
          return null;
        }
      }
      return invocation.proceed();
    }
  }
}
