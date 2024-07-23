package sparta.nbcamp.wachu.domain.pairing.service.v1

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepository
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.S3FilePath
import sparta.nbcamp.wachu.infra.aws.S3Service
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Service
class PairingServiceImpl(
    private val memberRepository: MemberRepository,
    private val pairingRepository: PairingRepository,
    private val s3Service: S3Service,
) : PairingService {
    override fun getPairingList(): List<PairingResponse> {
        val pairingList = pairingRepository.findAll()
        return pairingList.map { PairingResponse.from(it) }
    }

    @Transactional(readOnly = true)
    override fun getPairing(id: Long): PairingResponse {
        val pairing = pairingRepository.findById(id)
            ?: throw ModelNotFoundException("Pairing", id)
        return PairingResponse.from(pairing)
    }

    @Transactional
    override fun createPairing(userPrincipal: UserPrincipal, pairingRequest: PairingRequest,multipartFile: MultipartFile): PairingResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)

        val imageUrl= multipartFile.let { s3Service.upload(multipartFile,S3FilePath.PAIRING.path) }
        val pairing = PairingRequest.toEntity(member.id!!, pairingRequest, imageUrl)

        return PairingResponse.from(pairingRepository.save(pairing))
    }

    override fun deletePairing(userPrincipal: UserPrincipal, id: Long) {
        val pairing = pairingRepository.findById(id)
            ?: throw ModelNotFoundException("Pairing", id)
        check(
            pairing.hasPermission(
                userPrincipal.memberId,
                userPrincipal.role
            )
        ) { throw AccessDeniedException("not your pairing") }
        pairingRepository.delete(pairing)
    }
}