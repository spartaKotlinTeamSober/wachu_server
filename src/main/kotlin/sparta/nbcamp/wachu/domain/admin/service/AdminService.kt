package sparta.nbcamp.wachu.domain.admin.service

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.admin.dto.DesignatePromotionRequest
import sparta.nbcamp.wachu.domain.admin.model.WinePromotionJpaRepository
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException

@Service
class AdminService(
    private val wineRepository: WineRepository,
    private val winePromotionJpaRepository: WinePromotionJpaRepository
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
        return PromotionWineResponse.from(winePromotion)
    }
}