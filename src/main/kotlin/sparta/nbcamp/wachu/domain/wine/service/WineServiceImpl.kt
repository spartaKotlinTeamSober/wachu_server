package sparta.nbcamp.wachu.domain.wine.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.repository.WinePromotionRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.hateoas.WinePromotionAssembler
import sparta.nbcamp.wachu.infra.hateoas.WinePromotionModel
import java.io.IOException

@Service
class WineServiceImpl @Autowired constructor(
    private val wineRepository: WineRepository,
    private val winePromotionRepository: WinePromotionRepository,
    private val winePromotionAssembler: WinePromotionAssembler,
    private val pagedResourcesAssembler: PagedResourcesAssembler<WinePromotion>,
    // private val redisTemplate: RedisTemplate<String, Any>,
    // private val objectMapper: ObjectMapper
) : WineService {

    // private val objectMapper: ObjectMapper = ObjectMapper().apply {
    //     registerModule(JavaTimeModule())
    //     registerModule(Jackson2HalModule())
    //     configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    // }


    @Cacheable(value = ["wineCache"], key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction")
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

    @Cacheable(value = ["wineCacheById"], key = "#wineId")
    override fun getWineById(wineId: Long): WineResponse {
        val wine = wineRepository.findByIdOrNull(wineId) ?: throw ModelNotFoundException("wineRepository", id = wineId)
        return WineResponse.from(wine)
    }

    @Cacheable(value = ["compareWineCache"], key = "#wineIds[0] + '-' + #wineIds[1] ")
    override fun compareWine(wineIds: List<Long>): List<WineResponse> {

        val firstWine =
            wineRepository.findByIdOrNull(wineIds[0]) ?: throw ModelNotFoundException("wineRepository", id = wineIds[0])
        val secondWine =
            wineRepository.findByIdOrNull(wineIds[1]) ?: throw ModelNotFoundException("wineRepository", id = wineIds[1])
        val wines: List<Wine> = listOf(firstWine, secondWine)
        return wines.map { WineResponse.from(it) }
    }

    @Cacheable(value = ["promotionCache"], key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction")
    override fun getPromotionWineList(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): Page<WinePromotion> {

        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)
        val promotionsPage: Page<WinePromotion> = winePromotionRepository.findPromotionWineList(pageable)

        return promotionsPage
//        return pagedResourcesAssembler.toModel(promotionsPage, winePromotionAssembler)
    }

    // fun savePromotionWineListToCache(
    //     page: Int,
    //     size: Int,
    //     sortBy: String,
    //     direction: String,
    //     data: PagedModel<WinePromotionModel>
    // ) {
    //     val key = "$page-$size-$sortBy-$direction"
    //     val jsonData = try {
    //         objectMapper.writeValueAsString(data)
    //     } catch (e: IOException) {
    //         throw RuntimeException("Failed to serialize data", e)
    //     }
    //     redisTemplate.opsForValue().set(key, jsonData)
    // }
    //
    // fun getPromotionWineListFromCache(
    //     page: Int,
    //     size: Int,
    //     sortBy: String,
    //     direction: String
    // ): PagedModel<WinePromotionModel>? {
    //     val key = "$page-$size-$sortBy-$direction"
    //     val jsonData = redisTemplate.opsForValue().get(key) as String?
    //     return jsonData?.let {
    //         try {
    //             objectMapper.readValue(it, PagedModel::class.java) as PagedModel<WinePromotionModel>
    //         } catch (e: IOException) {
    //             throw RuntimeException("Failed to deserialize data", e)
    //         }
    //     }
    // }



    override fun recommendWine(request: RecommendWineRequest): List<WineResponse> {
        TODO("Not yet implemented")
    }

    private fun getDirection(sort: String) = when (sort.lowercase()) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }
}