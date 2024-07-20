package sparta.nbcamp.wachu.infra.openai.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingCompareDto
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData
import sparta.nbcamp.wachu.infra.openai.service.WineEmbeddingService

@RestController
class WineEmbeddingController(
    private val openAIService: WineEmbeddingService
) {
    @PostMapping("/admin/openai/embeddings")
    fun createEmbedding(): List<WineEmbeddingData> {
        return openAIService.convertJsonToEmbeddingJson()
    }

    @GetMapping("/admin/openai/embeddings/compare")
    fun createEmbedding(input1: String, input2: String): WineEmbeddingCompareDto {
        return openAIService.compareEmbedding(input1, input2)
    }
}