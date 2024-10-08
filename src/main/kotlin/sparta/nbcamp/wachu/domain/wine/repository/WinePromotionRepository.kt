package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

interface WinePromotionRepository {
    fun findPromotionWineList(pageable: Pageable): Page<PromotionWineResponse>

    fun findAllWithoutFetchJoinForTest(pageable: Pageable): Page<PromotionWineResponse>
}