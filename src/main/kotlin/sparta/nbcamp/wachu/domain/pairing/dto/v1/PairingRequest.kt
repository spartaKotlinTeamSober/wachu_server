package sparta.nbcamp.wachu.domain.pairing.dto.v1

import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.entity.Wine

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
) {
    fun toEntity(wine: Wine, memberId: Long, imageUrl: String?): Pairing {
        return Pairing(
            wine = wine,
            memberId = memberId,
            title = this.title,
            description = this.description,
            photoUrl = imageUrl,
        )
    }
}