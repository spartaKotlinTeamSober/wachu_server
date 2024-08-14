package sparta.nbcamp.wachu.domain.wine.dto

data class RecommendWineRequest(
    val preferWineId: Long,
    val priceWeight: Int,
    val tastyWeight: Int,
    val aromaWeight: Int,
)