package sparta.nbcamp.wachu.domain.wine.dto

import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.service.WineImageGetter
import sparta.nbcamp.wachu.infra.openai.dto.WineDataResponse
import java.io.Serializable

data class WineResponse(

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
    val imageUrl: String
) : Serializable {
    companion object {
        fun from(entity: Wine): WineResponse {
            return WineResponse(
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
                imageUrl = WineImageGetter.getWineImage(entity.wineType, entity.id)
            )
        }

        fun convert(dto: WineDataResponse): WineResponse {
            return WineResponse(
                id = dto.id,
                name = dto.name,
                sweetness = dto.sweetness,
                acidity = dto.acidity,
                body = dto.body,
                tannin = dto.tannin,
                wineType = dto.wineType,
                aroma = dto.aroma,
                price = dto.price,
                kind = dto.kind,
                style = dto.style,
                country = dto.country,
                region = dto.region,
                imageUrl = WineImageGetter.getWineImage(dto.wineType, dto.id)
            )
        }
    }
}
