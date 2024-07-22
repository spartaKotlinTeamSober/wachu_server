package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.entity.Wine

interface WineQueryDslRepository {
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

    fun getMinPrice(): Int

    fun getMaxPrice(): Int
}