package sparta.nbcamp.wachu.domain.wine.repository

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.wine.entity.Wine

interface WineJpaRepository : JpaRepository<Wine, Long>