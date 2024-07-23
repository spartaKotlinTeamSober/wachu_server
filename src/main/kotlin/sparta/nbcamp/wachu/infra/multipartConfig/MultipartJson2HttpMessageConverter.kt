package sparta.nbcamp.wachu.infra.multipartConfig

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import java.lang.reflect.Type

@Component
class MultipartJson2HttpMessageConverter(
    private val objectMapper: ObjectMapper
): AbstractJackson2HttpMessageConverter(objectMapper,MediaType.APPLICATION_OCTET_STREAM){

    @Override
    override fun canWrite(mediaType: MediaType?): Boolean {
        return false
    }

    @Override
    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }

    @Override
    override fun canWrite(type: Type?, clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }
}