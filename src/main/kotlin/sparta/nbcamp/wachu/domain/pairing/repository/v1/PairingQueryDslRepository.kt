package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingQueryDslRepository {
    fun findFetchJoin(wineId: Long, pageable: Pageable): Page<Pairing>
}