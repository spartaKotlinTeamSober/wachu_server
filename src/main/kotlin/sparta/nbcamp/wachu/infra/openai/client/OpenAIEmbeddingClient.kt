package sparta.nbcamp.wachu.infra.openai.client

import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.OpenAiEmbeddingOptions
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.infra.openai.common.utils.EmbeddingJsonHandler

@Component
class OpenAIEmbeddingClient(
    private val embeddingModel: OpenAiEmbeddingModel,
    private val jsonFileHandler: EmbeddingJsonHandler
) {
    private val embeddingCache = jsonFileHandler.readData().toMutableMap()

    fun inputListToEmbeddingData(inputList: List<String>): Map<String, List<Double>> {
        val dataMap = mutableMapOf<String, List<Double>>()

        inputList.forEach { input ->
            val property = input.trim()
            val embedding = embeddingCache[property]
                ?: run {
                    val data =
                        if (property.contains("price")) {
                            TODO("price의 확인 필요")
                            listOf()
                        } else {
                            convertInputToOpenAiEmbedding(property)
                        }

                    embeddingCache[property] = data
                    jsonFileHandler.writeData(embeddingCache)
                    data
                }

            dataMap[property] = embedding
        }

        return dataMap
    }

    fun convertInputToOpenAiEmbedding(input: String): List<Double> {
        return embeddingModel.call(
            EmbeddingRequest(
                listOf(input),
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build()
            )
        )
            .result
            .output
    }
}