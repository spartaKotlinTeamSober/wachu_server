package sparta.nbcamp.wachu.domain.pairing.service.v1

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

interface PairingService {
    fun getPairingList(): List<PairingResponse>
    fun getPairing(id: Long): PairingResponse
    fun createPairing(userPrincipal: UserPrincipal, pairingRequest: PairingRequest,multipartFile: MultipartFile): PairingResponse
    fun deletePairing(userPrincipal: UserPrincipal, id: Long)
}