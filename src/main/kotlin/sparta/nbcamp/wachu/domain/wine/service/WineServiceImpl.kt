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
import sparta.nbcamp.wachu.domain.wine.repository.WineQueryDslRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException

@Service
class WineServiceImpl @Autowired constructor(
    private val wineRepository: WineRepository,
    private val wineQueryDslRepository: WineQueryDslRepository
) : WineService {
    override fun getWineList(
        query: String,
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): List<WineResponse> {
        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)
        val wines: Page<Wine> = wineQueryDslRepository.searchWines(pageable = pageable, query = query)
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

    override fun getPopularWineList(
        page: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): List<WineResponse> {
        TODO("WinePromotion 엔티티를 만들지 않았으므로 주석처리함")
        // val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy) // WinePromotion 엔티티를 만들지 않았으므로 주석처리함
        // val winePromotionLIST = mutableListOf<Long>()
        // winePromotionLIST.add(winePromotionRepository.findAll().map{it.id})
        // val wineList: List<Wine> = wineRepository.findByIds(ids = winePromotionLIST)
        //
        // val wines: Page<Wine> = wineRepository.searchWinesFiltering(pageable = pageable, query = query , filter = wineList) //기존 querydsl 에 프로모션 id만 filter 링하기
        // return wines.map { WineResponse.from(it) }.toList()
    }

    override fun recommendWine(request: RecommendWineRequest): List<WineResponse> {
        TODO("Not yet implemented")
    }

    private fun getDirection(sort: String) = when (sort.lowercase()) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }
}