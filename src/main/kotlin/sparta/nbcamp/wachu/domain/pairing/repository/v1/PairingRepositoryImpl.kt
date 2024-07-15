package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.repository.findByIdOrNull
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

class PairingRepositoryImpl(
    val pairingJpaRepository: PairingJpaRepository,
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        return pairingJpaRepository.findByIdOrNull(id)
    }
}