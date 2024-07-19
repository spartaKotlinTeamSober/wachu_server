package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

@Repository
class PairingRepositoryImpl(
    private val pairingJpaRepository: PairingJpaRepository,
    private val pairingQueryDslRepository: PairingQueryDslRepository
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        return pairingJpaRepository.findByIdOrNull(id)
    }

    override fun findAll(pageable: Pageable): Page<Pairing> {
        return pairingQueryDslRepository.findPage(pageable)
    }

    override fun save(pairing: Pairing): Pairing {
        return pairingJpaRepository.save(pairing)
    }

    override fun delete(pairing: Pairing) {
        return pairingJpaRepository.delete(pairing)
    }
}