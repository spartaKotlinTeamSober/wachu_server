package sparta.nbcamp.wachu.infra.openai.controller

import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.OpenAiEmbeddingOptions
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OpenAIController(
    private val embeddingModel: OpenAiEmbeddingModel
) {
    @PostMapping("/test/openai/embeddings")
    fun createEmbedding(input: String): String {
        return embeddingModel.call(
            EmbeddingRequest(
                listOf(input),
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build()
            )
        )
            .result
            .output
            .toString()
    }

    @PostMapping("/test/openai/embeddings2")
    fun createEmbedding2(): String {
        val input = "이름: \"알토스 데 토로나, 마레멜\", 당도: 3, " +
            "산도: 2, 바디: 2, 타닌: 1, " +
            "종류: 화이트, 향기: 과일: {\"시트러스\"}, 가격: 65,000 " +
            "품종: 알바리뇨, 스타일: Spanish Albarino, 국가: 스페인, " +
            "지역: 리아스 바이사스"

        return embeddingModel.call(
            EmbeddingRequest(
                listOf(input),
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build()
            )
        )
            .result
            .output
            .toString()
    }
}