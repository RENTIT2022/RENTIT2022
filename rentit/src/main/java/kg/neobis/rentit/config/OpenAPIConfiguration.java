package kg.neobis.rentit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "RENTIT REST API", version = "${api.version}",
                contact = @Contact(name = "RENTIT", email = "rentit.kg@gmail.com", url = "https://www.youtube.com/watch?v=K_KYSvvDY8k"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), termsOfService = "${tos.uri}",
                description = "${api.description}")
)
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "Bearer Authorization";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description(
                                        "Предоставьте токен JWT. Токен JWT можно получить из API входа в систему.")
                                .bearerFormat("JWT")));
    }
}
