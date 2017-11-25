package com.validator.trade.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()            
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());
    }
     
    private ApiInfo apiInfo() {
         return new ApiInfo(
           "Trade Validation Service", 
           "Trade Validation Service", 
           "version 0.1", 
           "Terms of service", 
           new Contact("Michal Skaryszewski", "", "michal.skaryszewski@gmail.com"), 
           "MIT",
           "https://en.wikipedia.org/wiki/MIT_License");
    }
}