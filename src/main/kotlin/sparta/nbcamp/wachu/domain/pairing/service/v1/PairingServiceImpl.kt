package sparta.nbcamp.wachu.domain.pairing.service.v1

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse

@Service
class PairingServiceImpl : PairingService {
    override fun getPairingList(): List<PairingResponse> {
        TODO()
    }

    @Transactional
    override fun getPairing(id: Long): PairingResponse {
        TODO()
    }

    @Transactional
    override fun createPairing(pairingRequest: PairingRequest): PairingResponse {
        TODO()
    }

    @Transactional
    override fun deletePairing(id: Long) {
        TODO()
    }
}