package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse

@Service
class WineServiceImpl : WineService {
    override fun getWineList(): List<WineResponse> {
        TODO("Not yet implemented")
    }

    override fun getWineById(wineId: Long): WineResponse {
        TODO("Not yet implemented")
    }

    override fun compareWine(wineIds: List<Long>): List<WineResponse> {
        TODO("Not yet implemented")
    }

    override fun getPopularWineList(pageable: Pageable): List<WineResponse> {
        TODO("Not yet implemented")
    }
}