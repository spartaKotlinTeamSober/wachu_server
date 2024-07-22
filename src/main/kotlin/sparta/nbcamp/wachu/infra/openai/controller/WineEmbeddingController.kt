package sparta.nbcamp.wachu.infra.openai.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.infra.openai.dto.SimilarityRequest
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData
import sparta.nbcamp.wachu.infra.openai.service.WineEmbeddingService

@RestController
@PreAuthorize("hasRole('ADMIN')")
class WineEmbeddingController(
    private val embeddingService: WineEmbeddingService
) {
    @PostMapping("/admin/openai/embeddings")
    fun createEveryWineEmbedding(): List<WineEmbeddingData> {
        return embeddingService.createEveryWineEmbedding()
    }

    @GetMapping("/admin/openai/embeddings/{wineId}")
    fun createWineEmbedding(@PathVariable wineId: Long): WineEmbeddingData {
        return embeddingService.createWineEmbedding(wineId)
    }

    @GetMapping("/admin/openai/embeddings/similarity")
    fun getSimilarity(request: SimilarityRequest): Double {
        return embeddingService.getSimilarity(request.input1, request.input2)
    }
}