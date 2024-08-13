package sparta.nbcamp.wachu.infra.openai.common.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class WineEmbeddingJsonHandler(
    @Value("\${embedding.file.path}") private var filePath: String
) {
    private val mapper = jacksonObjectMapper().apply { registerModules(kotlinModule()) }
    private val file = File(filePath)

    init {
        if (!file.exists()) {
            file.createNewFile()
            mapper.writeValue(file, emptyMap<String, List<Double>>())
        }
    }

    fun readData(): Map<String, List<Double>> {
        return if (file.length() == 0L) {
            mapOf()
        } else {
            mapper.readValue(file, object : TypeReference<Map<String, List<Double>>>() {})
        }
    }

    fun writeData(data: Map<String, List<Double>>) {
        mapper.writeValue(file, data)
    }
}