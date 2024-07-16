package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val photo: String,
) {
    companion object {
        fun toPairing(memberId: Long, pairingRequest: PairingRequest): Pairing {
            return Pairing(
                wineId = pairingRequest.wineId,
                memberId = memberId,
                title = pairingRequest.title,
                description = pairingRequest.description,
                photoUrl = pairingRequest.photo,
            )
        }
    }
}