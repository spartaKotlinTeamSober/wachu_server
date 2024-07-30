package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.RefreshTokenRequest
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse

@RestController
@RequestMapping("/refresh-token")
class RefreshTokenController(
    private val refreshTokenService: RefreshTokenService,
) {
    @PostMapping
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenService.refreshAccessToken(request))
    }
}