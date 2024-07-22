package sparta.nbcamp.wachu.domain.pairing.service.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

interface PairingService {
    fun getPairingPage(pageable: Pageable): Page<PairingResponse>
    fun getPairing(id: Long): PairingResponse
    fun createPairing(userPrincipal: UserPrincipal, pairingRequest: PairingRequest): PairingResponse
    fun deletePairing(userPrincipal: UserPrincipal, id: Long)
}