package sparta.nbcamp.wachu.infra.hateoas

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer
import org.springframework.hateoas.config.EnableHypermediaSupport

@Configuration
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class HateoasConfig {

    // @Bean
    // fun pagedResourcesAssembler(): PagedResourcesAssembler<*> {
    //     return PagedResourcesAssembler<Any>(null, null)
    // }

    @Bean
    fun pageableCustomizer(): PageableHandlerMethodArgumentResolverCustomizer {
        return PageableHandlerMethodArgumentResolverCustomizer {
            it.setOneIndexedParameters(true)
        }
    }
}