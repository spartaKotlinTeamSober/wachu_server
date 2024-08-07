package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.ProfileResponse
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.emailcode.dto.SendCodeRequest
import sparta.nbcamp.wachu.domain.member.emailcode.service.CodeService
import sparta.nbcamp.wachu.domain.member.service.MemberService
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@RestController
class MemberController(
    private val memberService: MemberService,
    private val codeService: CodeService,
) {

    @PostMapping("/auth/sign-up/email-validation")
    fun sendValidationCode(@RequestBody request: SendCodeRequest): ResponseEntity<Any> {
        codeService.sendCode(request.email)
        return ResponseEntity.ok("Email send successfully")
    }

    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signup(request))
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<String> {
        val token = memberService.login(request)

        val cookie = ResponseCookie.from("refreshToken", token.refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(7 * 24 * 60 * 60)
            .path("/")
            .build()

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(token.accessToken)
    }

    @PostMapping(
        "/auth/profile",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestPart(name = "image") multipartFile: MultipartFile
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.uploadProfile(userPrincipal, multipartFile))
    }

    @GetMapping("/auth/profile/")
    fun getProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok().body(memberService.getProfile(userPrincipal))
    }

    @PostMapping("/auth/refresh-token")
    fun refreshAccessToken(
        @CookieValue("refreshToken") refreshToken: String
    ): ResponseEntity<String> {
        val tokenResponse = memberService.refreshAccessToken(refreshToken)
        return ResponseEntity.ok().body(tokenResponse.accessToken)
    }
}