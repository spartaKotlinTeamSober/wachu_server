package sparta.nbcamp.wachu.domain.wine.dto

import sparta.nbcamp.wachu.domain.wine.entity.Type
import sparta.nbcamp.wachu.domain.wine.entity.Wine

data class WineResponse(

    val id: Long,
    val sweetness: Int,
    val acidity: Int,
    val body: Int,
    val tannin: Int,
    val type: Type,
    val aroma: String,
    val price: Int?,
    val kind: String?,
    val style: String?,
    val country: String?,
    val region: String?,
) {

    companion object {
        fun from(entity: Wine): WineResponse {
            return WineResponse(
                id = entity.id,
                sweetness = entity.sweetness,
                acidity = entity.acidity,
                body = entity.body,
                tannin = entity.tannin,
                type = entity.type,
                aroma = entity.aroma,
                price = entity.price,
                kind = entity.kind,
                style = entity.style,
                country = entity.country,
                region = entity.region,
            )
        }
    }
}
