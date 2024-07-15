package sparta.nbcamp.wachu.domain.pairing.dto.v1

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val image: String,
)