package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingRepository {
    fun findById(id: Long): Pairing?
    fun findAll(pageable: Pageable): List<Pairing>
    fun save(pairing: Pairing): Pairing
    fun delete(pairing: Pairing)
}