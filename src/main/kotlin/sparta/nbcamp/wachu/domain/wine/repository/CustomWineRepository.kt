package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.wine.entity.Wine

interface CustomWineRepository {
    fun searchWines(query: String, pageable: Pageable): Page<Wine>
}