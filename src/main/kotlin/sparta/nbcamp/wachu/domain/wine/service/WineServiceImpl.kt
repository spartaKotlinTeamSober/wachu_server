package sparta.nbcamp.wachu.domain.wine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineRecommendResponse
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.repository.WinePromotionRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.openai.service.WineEmbeddingService
import sparta.nbcamp.wachu.infra.redis.common.RedisKeyConst

@Service
class WineServiceImpl @Autowired constructor(
    private val wineRepository: WineRepository,
    private val winePromotionRepository: WinePromotionRepository,
    private val wineEmbeddingService: WineEmbeddingService,
) : WineService {

    override fun getWineList(
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
    ): Page<WineResponse> {
        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)
        val wines: Page<Wine> = wineRepository.searchWines(
            query = query,
            price = price,
            acidity = acidity,
            body = body,
            sweetness = sweetness,
            tannin = tannin,
            type = type,
            pageable = pageable
        )
        return wines.map { WineResponse.from(it) }
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

    @Cacheable(
        value = [RedisKeyConst.PROMOTION_WINE_PREFIX],
        key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction"
    )
    override fun getPromotionWineList(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): Page<PromotionWineResponse> {
        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)
        return winePromotionRepository.findPromotionWineList(pageable)
    }

    override fun recommendWine(request: RecommendWineRequest): List<WineRecommendResponse> {
        return wineEmbeddingService.recommendWine(request)
            .map { WineRecommendResponse.from(it) }
    }

    private fun getDirection(sort: String) = when (sort.lowercase()) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }
}