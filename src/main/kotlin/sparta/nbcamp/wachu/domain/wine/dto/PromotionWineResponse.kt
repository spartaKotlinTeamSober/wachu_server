package sparta.nbcamp.wachu.domain.wine.dto

import sparta.nbcamp.wachu.domain.wine.entity.PromotionStatus
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import java.io.Serializable
import java.time.LocalDateTime

data class PromotionWineResponse(

    val wine: WineResponse,
    val promotionId: Long,
    val promotionStatus: PromotionStatus,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,

    ): Serializable {
    companion object {
        fun from(entity: WinePromotion): PromotionWineResponse {
            return PromotionWineResponse(
                wine = WineResponse.from(entity.wine),
                promotionId = entity.id,
                promotionStatus = entity.status,
                openedAt = entity.openedAt,
                closedAt = entity.closedAt,
            )
        }
    }
}

