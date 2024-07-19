package sparta.nbcamp.wachu.domain.pairing.repository.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingRepository {
    fun findById(id: Long): Pairing?
    fun findAll(): List<Pairing>
    fun save(pairing: Pairing): Pairing
    fun delete(pairing: Pairing)
    fun findList(page: Int, size: Int): List<Pairing>
}