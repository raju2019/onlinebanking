package com.bank.online.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerDocumentationConfig {

  /**
   * swagger api docket to scan all possible apis
   * @return Docket
   */
  @Bean
  public Docket apiDocumentNonProd() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .paths(regex("/.*"))
        .build();
  }

  /**
   * build api info for swagger ui.
   * @return ApiInfo
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Bank API")
        .description("Documentation for Bank API")
        .version("1.0")
        .build();
  }
}
