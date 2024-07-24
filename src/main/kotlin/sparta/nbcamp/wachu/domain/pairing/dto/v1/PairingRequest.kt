package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.entity.Wine

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val photo: String,
) {
    companion object {
        fun toEntity(wine: Wine, memberId: Long, pairingRequest: PairingRequest): Pairing {
            return Pairing(
                wine = wine,
                memberId = memberId,
                title = pairingRequest.title,
                description = pairingRequest.description,
                photoUrl = pairingRequest.photo,
            )
        }
    }
}