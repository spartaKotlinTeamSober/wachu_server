package sparta.nbcamp.wachu.infra.openai.client

import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.OpenAiEmbeddingOptions
import org.springframework.stereotype.Component

@Component
class OpenAIEmbeddingClient(
    private val embeddingModel: OpenAiEmbeddingModel,
) {
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