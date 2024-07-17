package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    override fun getWineList(
        query: String,
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): List<WineResponse> {
        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)
        val wines: Page<Wine> = wineRepository.searchWines(pageable = pageable, query = query)
        return wines.map { WineResponse.from(it) }.toList()
    }

    override fun getWineById(wineId: Long): WineResponse {
        val wine = wineRepository.findByIdOrNull(wineId) ?: throw ModelNotFoundException("wineRepository", id = wineId)
        return WineResponse.from(wine)
    }

    override fun compareWine(wineIds: List<Long>): List<WineResponse> {

        val firstWine =
            wineRepository.findByIdOrNull(wineIds[0]) ?: throw ModelNotFoundException("wineRepository", id = wineIds[0])
        val secondWine =
            wineRepository.findByIdOrNull(wineIds[1]) ?: throw ModelNotFoundException("wineRepository", id = wineIds[1])
        val wines: List<Wine> = listOf(firstWine, secondWine)
        return wines.map { WineResponse.from(it) }
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

    private fun getDirection(sort: String) = when (sort) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }
}