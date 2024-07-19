package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.data.domain.Page
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

interface WineService {
    fun getWineList(
        query: String,
        price: Int?,
        acidity: List<Int>?,
        body: List<Int>?,
        sweetness: List<Int>?,
        tannin: List<Int>?,
        type: String?,
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): Page<WineResponse>

    fun getWineById(wineId: Long): WineResponse
    fun compareWine(wineIds: List<Long>): List<WineResponse>
    fun getPromotionWineList(page: Int, size: Int, sortBy: String, direction: String): Page<WinePromotion>
    fun recommendWine(request: RecommendWineRequest): List<WineResponse>
}