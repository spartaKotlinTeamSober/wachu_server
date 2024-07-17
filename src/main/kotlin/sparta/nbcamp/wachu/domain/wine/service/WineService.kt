package sparta.nbcamp.wachu.domain.wine.service

import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse

interface WineService {
    fun getWineList(query: String, page: Int, size: Int, sortBy: String, direction: String): List<WineResponse>
    fun getWineById(wineId: Long): WineResponse
    fun compareWine(wineIds: List<Long>): List<WineResponse>
    fun getPopularWineList(query: String, page: Int, size: Int, sortBy: String, direction: String): List<WineResponse>
    fun recommendWine(request: RecommendWineRequest): List<WineResponse>
    fun postWineForTest(request: WineResponse)
}