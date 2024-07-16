package sparta.nbcamp.wachu.domain.member.service

import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse

interface MemberService {
    fun signup(request: SignUpRequest): SignUpResponse

    fun login(request: LoginRequest): TokenResponse
}