package sparta.nbcamp.wachu.infra.openai.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingCompareResponse
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData
import sparta.nbcamp.wachu.infra.openai.service.WineEmbeddingService

@RestController
class WineEmbeddingController(
    private val embeddingService: WineEmbeddingService
) {
    @PostMapping("/admin/openai/embeddings")
    fun createEmbedding(): List<WineEmbeddingData> {
        return embeddingService.convertJsonToEmbeddingJson()
    }

    @GetMapping("/admin/openai/embeddings/compare")
    fun createEmbedding(input1: String, input2: String): WineEmbeddingCompareResponse {
        return embeddingService.compareEmbedding(input1, input2)
    }

    @GetMapping("/admin/openai/embeddings")
    fun getEmbedding(property: String): WineEmbeddingData {
        return embeddingService.retrieveEmbedding(property)
    }
}