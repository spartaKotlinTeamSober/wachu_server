package sparta.nbcamp.wachu.domain.pairing.controller.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.domain.pairing.service.v1.PairingService

@RestController
@RequestMapping("/api/v1/pairings")
class PairingController(
    private val pairingService: PairingService,
) {
    fun getPairingList(): ResponseEntity<List<PairingResponse>> {
        TODO()
    }

    @GetMapping("/{pairing_id}")
    fun getPairing(
        @PathVariable id: Long
    ): ResponseEntity<PairingResponse> {
        TODO()
    }

    @GetMapping("/wines")
    fun createPairing(
        @RequestBody pairingRequest: PairingRequest
    ): ResponseEntity<PairingResponse> {
        TODO()
    }

    @GetMapping("/{pairing_id}")
    fun deletePairing(
        @PathVariable id: Long
    ) {
        TODO()
    }
}