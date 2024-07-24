package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import java.time.LocalDateTime

data class PairingResponse(
    val id: Long,
    val wine: WineResponse,
    val memberId: Long,
    val title: String,
    val description: String,
    val photoUrl: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(pairing: Pairing): PairingResponse {
            return PairingResponse(
                id = pairing.id!!,
                wine = WineResponse.from(pairing.wine),
                memberId = pairing.memberId,
                title = pairing.title,
                description = pairing.description,
                photoUrl = pairing.photoUrl,
                createdAt = pairing.createdAt,
            )
        }
    }
}