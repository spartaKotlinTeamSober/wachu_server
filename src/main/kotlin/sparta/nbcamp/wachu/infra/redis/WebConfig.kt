// package sparta.nbcamp.wachu.infra.redis
//
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.http.converter.HttpMessageConverter
// import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//
// @Configuration
// class WebConfig : WebMvcConfigurer {
//
//     @Bean
//     fun customCacheHttpMessageConverter(): CustomPagedModelHttpMessageConverter {
//         return CustomPagedModelHttpMessageConverter()
//     }
//
//     override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
//         // 커스텀 컨버터를 등록합니다.
//         converters.add(customCacheHttpMessageConverter())
//         // 기본 JSON Converter를 추가합니다.
//         converters.add(MappingJackson2HttpMessageConverter())
//     }
// }