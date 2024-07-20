package sparta.nbcamp.wachu.infra.openai.dto

data class WineEmbeddingCompareResponse(
    val similarity: Double,
    val target: List<Double>,
    val compare: List<Double>,
)