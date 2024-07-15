package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

@Repository
class PairingRepositoryImpl(
    val pairingJpaRepository: PairingJpaRepository,
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        return pairingJpaRepository.findByIdOrNull(id)
    }
}