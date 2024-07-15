package sparta.nbcamp.wachu.domain.member.service

import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest

interface MemberService {
    fun signup(request: SignUpRequest)

    fun login(request: LoginRequest)
}