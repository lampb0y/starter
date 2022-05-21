/*
* Copyright naswork 2020 - All Rights Reserved.
* The copyright to the computer program(s) herein
* is the property of naswork.The programs may
* be used and/or copied only with written permission
* from naswork or in accordance with the terms
* and conditions stipulated in the agreement/contract
* under which the program(s) have been supplied.
*/
package com.naswork.starter.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean辅助类，用于非spring管理的类来获取spring管理的bean
 * @author eyaomai
 *
 */
@Component
public class BeanUtils implements ApplicationContextAware {

  private static ApplicationContext context;
  
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
 
  public static <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }
}
