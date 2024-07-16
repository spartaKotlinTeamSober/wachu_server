package sparta.nbcamp.wachu.domain.pairing.service.v1

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepository
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Service
class PairingServiceImpl(
    private val memberRepository: MemberRepository,
    private val pairingRepository: PairingRepository,
) : PairingService {
    override fun getPairingList(): List<PairingResponse> {
        val pairingList = pairingRepository.findAll()
        return pairingList.map { PairingResponse.from(it) }
    }

    @Transactional(readOnly = true)
    override fun getPairing(id: Long): PairingResponse {
        val pairing = pairingRepository.findById(id)
            ?: throw IllegalArgumentException("Pairing not found")
        return PairingResponse.from(pairing)
    }

    @Transactional
    override fun createPairing(userPrincipal: UserPrincipal, pairingRequest: PairingRequest): PairingResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw IllegalArgumentException("Member not found")
        val pairing = PairingRequest.toPairing(member.id, pairingRequest)
        return PairingResponse.from(pairingRepository.save(pairing))
    }

    override fun deletePairing(userPrincipal: UserPrincipal, id: Long) {
        val pairing = pairingRepository.findById(id)
            ?: throw IllegalArgumentException("Pairing not found")
        if (pairing.memberId != userPrincipal.memberId) {
            throw IllegalArgumentException("not your pairing")
        }
        pairingRepository.delete(pairing)
    }
}