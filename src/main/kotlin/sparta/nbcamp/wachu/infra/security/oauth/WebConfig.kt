package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:8080")  // 패턴 매칭을 사용하여 허용할 출처 설정
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}