package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.wine.entity.Wine

@Repository
class WineRepositoryImpl(
    private val wineJpaRepository: WineJpaRepository,
    private val wineQueryDslRepository: WineQueryDslRepository
) : WineRepository {

    override fun findAll(pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findAll(pageable)
    }

    override fun findByNameContaining(query: String, pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findByNameContaining(query, pageable)
    }

    override fun findByIdOrNull(id: Long): Wine? {
        return wineJpaRepository.findByIdOrNull(id)
    }

    override fun searchWines(
        query: String,
        price: Int?,
        acidity: List<Int>?,
        body: List<Int>?,
        sweetness: List<Int>?,
        tannin: List<Int>?,
        type: String?,
        pageable: Pageable
    ): Page<Wine> {
        return wineQueryDslRepository.searchWines(
            query = query,
            price = price,
            acidity = acidity,
            body = body,
            sweetness = sweetness,
            tannin = tannin,
            type = type,
            pageable = pageable
        )
    }
}
