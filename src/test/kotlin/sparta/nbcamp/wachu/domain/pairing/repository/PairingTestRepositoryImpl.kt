package sparta.nbcamp.wachu.domain.pairing.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepository
import java.util.logging.Level
import java.util.logging.Logger

class PairingTestRepositoryImpl(
    private val pairing: Pairing,
    private val pairingPage: Page<Pairing>
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        if (id !in 1..10) return null
        return pairing
    }

    override fun findAll(pageable: Pageable): Page<Pairing> {
        return pairingPage
    }

    override fun save(pairing: Pairing): Pairing {
        return pairing.apply { id = 0L }
    }

    override fun delete(pairing: Pairing) {
        Logger.getLogger("PairingTestRepositoryImpl").log(Level.INFO, "delete")
    }
}