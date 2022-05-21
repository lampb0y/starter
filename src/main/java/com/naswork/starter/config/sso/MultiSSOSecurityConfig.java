package com.naswork.starter.config.sso;

import com.naswork.starter.config.AuthExceptionEntryPoint;
import com.naswork.starter.config.SSOProperties;
import com.naswork.starter.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SSO通用验证配置
 * @author eyaomai
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@ConditionalOnProperty(prefix = "sso", value = "multi-sso",
//      havingValue = "true", matchIfMissing = false)
@ConditionalOnExpression("'${sso.multi-sso}'.equals('enabled') ")
public class MultiSSOSecurityConfig extends WebSecurityConfigurerAdapter {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private SSOProperties multiSSOProperties;

  public MultiSSOSecurityConfig() {
    logger.info("init SSO security config bean");
  }
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .authenticationEventPublisher(new NoopAuthenticationEventPublisher())
        .authenticationProvider(new JwtAuthenticationProvider(multiSSOProperties.getPublicKey()));
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //logger.info("ApiMatcher:{}", securityProperties.getApiMatcher());
    if (securityProperties.getWhiteList() != null && securityProperties
        .getWhiteList().size() > 0) {
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

  @Bean
  public MultiSSOAuthenticationFilter customizedAuthenticationProcessingFilter() throws Exception {
    return new MultiSSOAuthenticationFilter(authenticationManagerBean(),
        multiSSOProperties);
  }

  /**
   * 认证事件发布者
   * @author eyaomai
   *
   */
  public class NoopAuthenticationEventPublisher implements AuthenticationEventPublisher {

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception,
                                             Authentication authentication) {
    }
  }  
}
