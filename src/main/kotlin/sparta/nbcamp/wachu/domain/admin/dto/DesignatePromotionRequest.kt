package sparta.nbcamp.wachu.domain.admin.dto

import sparta.nbcamp.wachu.domain.wine.entity.PromotionStatus
import java.time.LocalDateTime

data class DesignatePromotionRequest(
    val wineId: Long,
    val status: PromotionStatus,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime
)
