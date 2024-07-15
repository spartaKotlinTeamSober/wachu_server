package sparta.nbcamp.wachu.domain.pairing.repository.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingRepository {
    fun findById(id: Long): Pairing?
}