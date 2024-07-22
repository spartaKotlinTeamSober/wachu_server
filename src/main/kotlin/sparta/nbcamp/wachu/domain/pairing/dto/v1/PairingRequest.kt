package sparta.nbcamp.wachu.domain.pairing.dto.v1

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
) {
    companion object {
        fun toEntity(memberId: Long, pairingRequest: PairingRequest,imageUrl:String): Pairing {
            return Pairing(
                wineId = pairingRequest.wineId,
                memberId = memberId,
                title = pairingRequest.title,
                description = pairingRequest.description,
                photoUrl = imageUrl,
            )
        }
    }
}