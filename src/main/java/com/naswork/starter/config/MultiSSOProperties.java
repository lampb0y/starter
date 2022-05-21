///*
//* Copyright naswork 2020 - All Rights Reserved.
//* The copyright to the computer program(s) herein
//* is the property of naswork.The programs may
//* be used and/or copied only with written permission
//* from naswork or in accordance with the terms
//* and conditions stipulated in the agreement/contract
//* under which the program(s) have been supplied.
//*/
//package com.naswork.starter.config;
//
//import lombok.Data;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//
//import javax.validation.constraints.NotNull;
//import java.util.List;
//
///**
//*@return
//*ChanningXie
//*@Date:2021/1/25 10:21
//*/
//
//@Component
//@Configuration
//@ConfigurationProperties(prefix = "multi-sso")
//@Validated
//@Data
//public class MultiSSOProperties {
//
//  private List<SSOServerProperties> servers;
//
//
//
//  public List<SSOServerProperties> getServers() {
//    return servers;
//  }
//
//  public void setServers(List<SSOServerProperties> servers) {
//    this.servers = servers;
//  }
//
//
//}
