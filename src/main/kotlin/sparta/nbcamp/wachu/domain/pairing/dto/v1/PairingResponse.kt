package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.member.dto.ProfileResponse
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import java.time.LocalDateTime

data class PairingResponse(
    val id: Long,
    val wine: WineResponse,
    val member: ProfileResponse,
    val title: String,
    val description: String,
    val photoUrl: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(pairing: Pairing, member: Member): PairingResponse {
            return PairingResponse(
                id = pairing.id!!,
                wine = WineResponse.from(pairing.wine),
                member = ProfileResponse.from(member),
                title = pairing.title,
                description = pairing.description,
                photoUrl = pairing.photoUrl,
                createdAt = pairing.createdAt,
            )
        }
    }
}