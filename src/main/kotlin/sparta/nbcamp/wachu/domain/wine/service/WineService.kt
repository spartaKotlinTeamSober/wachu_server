package sparta.nbcamp.wachu.domain.wine.service

import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse

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
    ): List<WineResponse>

    fun getWineById(wineId: Long): WineResponse
    fun compareWine(wineIds: List<Long>): List<WineResponse>
    fun getPopularWineList(page: Int, size: Int, sortBy: String, direction: String): List<WineResponse>
    fun recommendWine(request: RecommendWineRequest): List<WineResponse>
}