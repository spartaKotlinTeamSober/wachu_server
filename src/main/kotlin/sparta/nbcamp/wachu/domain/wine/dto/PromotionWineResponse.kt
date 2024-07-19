package sparta.nbcamp.wachu.domain.wine.dto

import sparta.nbcamp.wachu.domain.wine.entity.PromotionStatus
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import java.time.LocalDateTime

data class PromotionWineResponse(

    val wineId: Long,
    val name: String,
    val sweetness: Int,
    val acidity: Int,
    val body: Int,
    val tannin: Int,
    val wineType: WineType,
    val aroma: String,
    val price: Int?,
    val kind: String?,
    val style: String?,
    val country: String?,
    val region: String?,
    val promotionId: Long,
    val promotionStatus: PromotionStatus,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,

    ) {
    companion object {
        fun from(entity: WinePromotion): PromotionWineResponse {
            return PromotionWineResponse(
                wineId = entity.wine.id,
                name = entity.wine.name,
                sweetness = entity.wine.sweetness,
                acidity = entity.wine.acidity,
                body = entity.wine.body,
                tannin = entity.wine.tannin,
                wineType = entity.wine.wineType,
                aroma = entity.wine.aroma,
                price = entity.wine.price,
                kind = entity.wine.kind,
                style = entity.wine.style,
                country = entity.wine.country,
                region = entity.wine.region,
                promotionId = entity.id,
                promotionStatus = entity.status,
                openedAt = entity.openedAt,
                closedAt = entity.closedAt,

                )
        }
    }
}

