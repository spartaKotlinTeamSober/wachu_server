package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class WineRepositoryImpl(
    private val wineJpaRepository: WineJpaRepository
) : WineRepository, CustomWineRepository, QueryDslSupport() {
    private val wine = QWine.wine

    override fun findAll(pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findAll(pageable)
    }

    override fun findByNameContaining(query: String, pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findByNameContaining(query, pageable)
    }
}