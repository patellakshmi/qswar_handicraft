package com.qswar.hc.config;

import com.newrelic.api.agent.Trace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Trace
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.qswar.hc"))
        .paths(regex("/.*"))
        .build()
        .apiInfo(apiEndPointsInfo());
  }

  @Trace
  private ApiInfo apiEndPointsInfo() {
    return new ApiInfoBuilder()
        .title("QSwar-HC APIs")
        .description("QSwar-HC: Precision in engineering for excellent")
        .contact(new Contact("Ms. Nagina Patel", "www.qswar.com", "manager@qswar.com"))
        .license("Quantum Software For Worldwide Advancement & Revolution Pvt. Ltd.")
        .version("v1")
        .build();
  }
}
