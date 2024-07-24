package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080")  // 특정 출처를 명시적으로 설정
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}