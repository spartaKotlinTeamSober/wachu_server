package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingRepository {
    fun findAll(pageable: Pageable): Page<Pairing>
    fun findFetchJoin(wineId: Long, pageable: Pageable): Page<Pairing>
    fun findById(id: Long): Pairing?
    fun save(pairing: Pairing): Pairing
    fun delete(pairing: Pairing)
    fun findByWineId(wineId: Long, pageable: Pageable): Page<Pairing>
}