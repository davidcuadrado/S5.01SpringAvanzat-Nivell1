package cat.itacademy.s05.t01.n01.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("blackjack-api")
                .pathsToMatch("/game/**", "/player/**", "/ranking")
                .build();
    }
}