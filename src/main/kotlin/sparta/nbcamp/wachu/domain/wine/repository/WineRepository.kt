package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

interface WineRepository {
    fun findAll(pageable: Pageable): Page<Wine>

    fun findByNameContaining(query: String, pageable: Pageable): Page<Wine>

    fun findByIdOrNull(id: Long): Wine?

    fun searchWines(
        query: String,
        price: Int?,
        acidity: List<Int>?,
        body: List<Int>?,
        sweetness: List<Int>?,
        tannin: List<Int>?,
        type: String?,
        pageable: Pageable,
    ): Page<Wine>

    fun findPromotionWineList(pageable: Pageable): Page<WinePromotion>
}