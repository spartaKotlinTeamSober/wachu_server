package sparta.nbcamp.wachu.domain.admin.model

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

interface WinePromotionJpaRepository : JpaRepository<WinePromotion, Long>