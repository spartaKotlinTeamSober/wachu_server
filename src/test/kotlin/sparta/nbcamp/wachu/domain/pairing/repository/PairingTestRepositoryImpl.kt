package sparta.nbcamp.wachu.domain.pairing.repository

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepository
import java.util.logging.Level
import java.util.logging.Logger

class PairingTestRepositoryImpl(
    private val pairing: Pairing,
    private val pairingList: List<Pairing>
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        if (id !in 1..10) return null
        return pairing
    }

    override fun findAll(): List<Pairing> {
        return pairingList
    }

    override fun save(pairing: Pairing): Pairing {
        return pairing.apply { id = 0L }
    }

    override fun delete(pairing: Pairing) {
        Logger.getLogger("PairingTestRepositoryImpl").log(Level.INFO, "delete")
    }
}