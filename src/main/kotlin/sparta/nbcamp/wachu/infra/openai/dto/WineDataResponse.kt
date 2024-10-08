package sparta.nbcamp.wachu.infra.openai.dto

import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import java.io.Serializable

data class WineDataResponse(
    val id: Long,
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
) : Serializable {
    companion object {

        fun from(entity: Wine): WineDataResponse {
            return WineDataResponse(
                id = entity.id,
                name = entity.name,
                sweetness = entity.sweetness,
                acidity = entity.acidity,
                body = entity.body,
                tannin = entity.tannin,
                wineType = entity.wineType,
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
