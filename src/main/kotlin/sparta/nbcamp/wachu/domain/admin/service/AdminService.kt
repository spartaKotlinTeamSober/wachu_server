package sparta.nbcamp.wachu.domain.admin.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.admin.dto.DesignatePromotionRequest
import sparta.nbcamp.wachu.domain.admin.model.WinePromotionJpaRepository
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.redis.EvictCache

@Service
class AdminService(
    private val wineRepository: WineRepository,
    private val winePromotionJpaRepository: WinePromotionJpaRepository,
    private val evictCache: EvictCache,
) {

    fun designatePromotion(request: DesignatePromotionRequest): PromotionWineResponse {
        val wine = wineRepository.findByIdOrNull(request.wineId) ?: throw ModelNotFoundException(
            modelName = "Wine",
            id = request.wineId
        )

        val winePromotion = winePromotionJpaRepository.save(
            WinePromotion(
                wine = wine,
                openedAt = request.openedAt,
                closedAt = request.closedAt,
                status = request.status,
            )
        )
        evictCache.evictCaches(deleteCache = "promotionCache")
        return PromotionWineResponse.from(winePromotion)
    }
}