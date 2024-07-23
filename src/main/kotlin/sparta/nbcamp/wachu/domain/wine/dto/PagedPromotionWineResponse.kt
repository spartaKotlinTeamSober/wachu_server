package sparta.nbcamp.wachu.domain.wine.dto

import java.io.Serializable

data class PagedPromotionWineResponse(
    val content: List<PromotionWineResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int
) : Serializable