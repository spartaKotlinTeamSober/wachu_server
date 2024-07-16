package sparta.nbcamp.wachu.domain.review.dto.v1

data class ReviewResponse(
    val id: Long,
    val wineId: Long,
    val memberId: Long,
    val title: String,
    val description: String,
    val score: Double,
    val imageUrl: String,
    val createdAt: String,
)
