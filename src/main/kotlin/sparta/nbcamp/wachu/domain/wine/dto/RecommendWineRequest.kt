package sparta.nbcamp.wachu.domain.wine.dto

data class RecommendWineRequest(

    val preferWineId: Long,
    val wineType: String,
    val priceWeight: Float,
    val sweetnessWeight: Float,
    val acidityWeight: Float,
    val bodyWeight: Float,
    val tanninWeight: Float,
)