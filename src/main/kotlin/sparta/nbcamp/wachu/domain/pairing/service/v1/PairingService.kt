package sparta.nbcamp.wachu.domain.pairing.service.v1

import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse

interface PairingService {
    fun getPairingList(): List<PairingResponse>
    fun getPairing(id: Long): PairingResponse
    fun createPairing(pairingRequest: PairingRequest): PairingResponse
    fun deletePairing(id: Long)
}