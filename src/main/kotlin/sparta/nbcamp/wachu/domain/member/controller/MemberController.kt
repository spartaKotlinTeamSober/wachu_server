package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.service.MemberService

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/auth/sign-up")
    fun signUp(@RequestBody request: SignUpRequest) {
        return memberService.signup(request)
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest) {
        return memberService.login(request)
    }
}