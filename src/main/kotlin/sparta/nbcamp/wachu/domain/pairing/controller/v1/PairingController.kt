package sparta.nbcamp.wachu.domain.pairing.controller.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
    @GetMapping
    fun getPairingList(): ResponseEntity<List<PairingResponse>> {
        TODO()
    }

    @GetMapping("/{pairingId}")
    fun getPairing(
        @PathVariable pairingId: Long
    ): ResponseEntity<PairingResponse> {
        TODO()
    }

    @PostMapping
    fun createPairing(
        @RequestBody pairingRequest: PairingRequest
    ): ResponseEntity<PairingResponse> {
        TODO()
    }

    @DeleteMapping("/{pairingId}")
    fun deletePairing(
        @PathVariable pairingId: Long
    ) {
        TODO()
    }
}