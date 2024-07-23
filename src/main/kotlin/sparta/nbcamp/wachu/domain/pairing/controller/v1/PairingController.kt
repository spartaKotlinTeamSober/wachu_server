package sparta.nbcamp.wachu.domain.pairing.controller.v1

import com.fasterxml.jackson.databind.util.JSONPObject
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingResponse
import sparta.nbcamp.wachu.domain.pairing.service.v1.PairingService
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@RestController
@RequestMapping("/api/v1/pairings")
class PairingController(
    private val pairingService: PairingService,
) {
    @GetMapping
    fun getPairingPage(
        @PageableDefault(page = 0, size = 10)
        pageable: Pageable
    ): ResponseEntity<Page<PairingResponse>> {
        return ResponseEntity.ok(pairingService.getPairingPage(pageable))
    }

    @GetMapping("/{pairingId}")
    fun getPairing(
        @PathVariable pairingId: Long
    ): ResponseEntity<PairingResponse> {
        return ResponseEntity.ok(pairingService.getPairing(pairingId))
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createPairing(
        @AuthenticationPrincipal userprincipal: UserPrincipal,
        @RequestPart pairingRequest: PairingRequest,
        @RequestPart(name = "image", required = false) multipartFile: MultipartFile
    ): ResponseEntity<PairingResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pairingService.createPairing(userprincipal, pairingRequest,multipartFile))
    }

    @DeleteMapping("/{pairingId}")
    fun deletePairing(
        @AuthenticationPrincipal userprincipal: UserPrincipal,
        @PathVariable pairingId: Long
    ): ResponseEntity<Unit> {
        pairingService.deletePairing(userprincipal, pairingId)
        return ResponseEntity.noContent().build()
    }
}
