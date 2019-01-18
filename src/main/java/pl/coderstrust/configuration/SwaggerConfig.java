package pl.coderstrust.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
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

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Invoices API")
        .description("This panel allow you to test all of possibilities in Invoice API."
            + "\nIncluding: \n- Find all invoices in database \n- Add new invoices to database"
            + "\n- Update existing invoice, \n- Remove invoice from database.")
        .version("1.0.0")
        .contact(new Contact("Group 7, team C", "http://teamc.com", "teamc@gmail.com"))
        .build();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false);
  }
}
