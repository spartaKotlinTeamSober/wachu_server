package sparta.nbcamp.wachu.domain.review.dto.v1

data class ReviewRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val score: Double,
    val image: String,
)
