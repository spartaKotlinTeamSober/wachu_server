// package sparta.nbcamp.wachu.infra.redis
//
// import org.springframework.http.HttpInputMessage
// import org.springframework.http.HttpOutputMessage
// import org.springframework.http.MediaType
// import org.springframework.http.converter.HttpMessageConverter
// import org.springframework.http.converter.HttpMessageNotReadableException
// import org.springframework.http.converter.HttpMessageNotWritableException
// import java.io.IOException
// import com.fasterxml.jackson.databind.ObjectMapper
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
// import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
// import org.springframework.hateoas.PagedModel
// import sparta.nbcamp.wachu.infra.hateoas.WinePromotionModel
//
// class CustomPagedModelHttpMessageConverter : HttpMessageConverter<PagedModel<WinePromotionModel>> {
//
//     private val objectMapper: ObjectMapper = ObjectMapper().apply {
//         registerModule(JavaTimeModule())
//         registerModule(Jackson2HalModule())
//         configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//     }
//
//     override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
//         return PagedModel::class.java == clazz
//     }
//
//     override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
//         return PagedModel::class.java == clazz
//     }
//
//     override fun getSupportedMediaTypes(): MutableList<MediaType> {
//         TODO("Not yet implemented")
//     }
//
//     override fun read(clazz: Class<out PagedModel<WinePromotionModel>>, inputMessage: HttpInputMessage): PagedModel<WinePromotionModel> {
//         try {
//             return objectMapper.readValue(inputMessage.body, PagedModel::class.java) as PagedModel<WinePromotionModel>
//         } catch (e: IOException) {
//             throw HttpMessageNotReadableException("Failed to read JSON", e)
//         }
//     }
//
//     override fun write(t: PagedModel<WinePromotionModel>, contentType: MediaType?, outputMessage: HttpOutputMessage) {
//         try {
//             objectMapper.writeValue(outputMessage.body, t)
//         } catch (e: IOException) {
//             throw HttpMessageNotWritableException("Failed to write JSON", e)
//         }
//     }
// }
