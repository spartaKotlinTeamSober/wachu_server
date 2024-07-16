package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse

interface WineService {
    fun getWineList(query: String?, pageable: Pageable): List<WineResponse>
    fun getWineById(wineId: Long): WineResponse
    fun compareWine(wineIds: List<Long>): List<WineResponse>
    fun getPopularWineList(pageable: Pageable): List<WineResponse>
    fun recommendWine(request: RecommendWineRequest): List<WineResponse>
    fun postWineForTest(request: WineResponse)
}