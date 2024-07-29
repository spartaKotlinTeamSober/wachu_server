package sparta.nbcamp.wachu.domain.wine

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository

class WineTestRepositoryImpl(
    private val wineJpaRepository: WineJpaRepository
) : WineRepository {
    override fun findAll(pageable: Pageable): Page<Wine> {
        return Page.empty()
    }

    override fun findByNameContaining(query: String, pageable: Pageable): Page<Wine> {
        return Page.empty()
    }

    override fun findByIdOrNull(id: Long): Wine? {
        return null
    }

    override fun searchWines(
        query: String,
        price: Int?,
        acidity: List<Int>?,
        body: List<Int>?,
        sweetness: List<Int>?,
        tannin: List<Int>?,
        type: String?,
        pageable: Pageable,
    ): Page<Wine> {
        return Page.empty()
    }

    override fun findMinPrice(): Int {
        return 0
    }

    override fun findMaxPrice(): Int {
        return 0
    }

    fun deleteAllforTest() {
        wineJpaRepository.deleteAll()
    }

    fun saveAllforTest(wines: List<Wine>): List<Wine> {
        return wineJpaRepository.saveAll(wines)
    }
}