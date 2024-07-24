package sparta.nbcamp.wachu.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(
            SecurityRequirement().addList("Bearer Authentication")
        )
        .addSecurityItem(
            SecurityRequirement().addList("oauth2schema")
        )
        .components(
            Components()
                .addSecuritySchemes(
                    "Bearer Authentication",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .`in`(SecurityScheme.In.HEADER)
                        .name("Authorization")
                )
                .addSecuritySchemes(
                    "oauth2schema",
                    SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(
                            io.swagger.v3.oas.models.security.OAuthFlows()
                                .authorizationCode(
                                    io.swagger.v3.oas.models.security.OAuthFlow()
                                        .authorizationUrl("https://nid.naver.com/oauth2.0/authorize")
                                        .tokenUrl("https://nid.naver.com/oauth2.0/token")
                                )
                        )
                )
        )
        .info(
            Info()
                .title("Wachu API")
                .description("Wachu API schema")
                .version("1.0.0")
        )
}
