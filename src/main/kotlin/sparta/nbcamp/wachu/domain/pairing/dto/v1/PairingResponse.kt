package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import java.time.LocalDateTime

data class PairingResponse(
    val id: Long,
    val wineId: Long,
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
                wineId = pairing.wine.id,
                memberId = pairing.memberId,
                title = pairing.title,
                description = pairing.description,
                photoUrl = pairing.photoUrl,
                createdAt = pairing.createdAt,
            )
        }
    }
}