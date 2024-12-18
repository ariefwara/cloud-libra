package id.my.ariefwara.cloud.libra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI cloudLibraApiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cloud Libra API")
                        .description("API documentation for managing a library system, including borrower and book operations.")
                        .version("1.0.0"));
    }
}
