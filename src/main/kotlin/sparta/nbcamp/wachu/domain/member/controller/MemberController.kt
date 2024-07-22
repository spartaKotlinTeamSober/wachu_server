package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.dto.*
import sparta.nbcamp.wachu.domain.member.service.MemberService
import sparta.nbcamp.wachu.infra.aws.S3Service
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signup(request))
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(request))
    }

    @PostMapping("/auth/profile",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun uploadProfile(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestPart(name = "image") multipartFile: MultipartFile
    ): ResponseEntity<ProfileResponse>{
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.uploadProfile(userPrincipal,multipartFile))
    }
}