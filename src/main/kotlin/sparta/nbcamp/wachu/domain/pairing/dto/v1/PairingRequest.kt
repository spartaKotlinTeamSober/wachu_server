package sparta.nbcamp.wachu.domain.pairing.dto.v1

import java.io.File

data class PairingRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val image: File
)