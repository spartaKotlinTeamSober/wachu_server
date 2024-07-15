package sparta.nbcamp.wachu.domain.pairing.dto.v1

data class PairingResponse(
    val id: Long,
    val wineId: Long,
    val memberId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val createdAt: String,
)