package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException

@Service
class WineServiceImpl @Autowired constructor(
    private val wineRepository: WineRepository,
    private val wineJpaRepository: WineJpaRepository // TODO() 해당 라인은 테스트 후에 필히 삭제할것
) : WineService {
    override fun getWineList(query: String?, pageable: Pageable): List<WineResponse> {
        val wines: Page<Wine>
        if (query.isNullOrEmpty()) wines = wineRepository.findAll(pageable)
        else wines = wineRepository.searchWines(query, pageable)
        return wines.map { WineResponse.from(it) }.toList()
    }

    override fun getWineById(wineId: Long): WineResponse {
        val wine = wineRepository.findByIdOrNull(wineId) ?: throw ModelNotFoundException("wineRepository", id = wineId)
        return WineResponse.from(wine)
    }

    override fun compareWine(wineIds: List<Long>): List<WineResponse> {
        TODO("Not yet implemented")
    }

    override fun getPopularWineList(pageable: Pageable): List<WineResponse> {
        TODO("Not yet implemented")
    }

    override fun recommendWine(request: RecommendWineRequest): List<WineResponse> {
        TODO("Not yet implemented")
    }

    override fun postWineForTest(request: WineResponse) {

        wineJpaRepository.save(
            Wine(
                name = request.name,
                sweetness = request.sweetness,
                acidity = request.acidity,
                aroma = request.aroma,
                body = request.body,
                tannin = request.tannin,
                id = request.id,
                country = request.country,
                embedding = null,
                kind = request.kind,
                price = request.price,
                region = request.region,
                style = request.style
            )
        )
    }
}