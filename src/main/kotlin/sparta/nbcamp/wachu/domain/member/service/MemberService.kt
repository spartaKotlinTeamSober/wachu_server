package sparta.nbcamp.wachu.domain.member.service

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest

@Service
interface MemberService {
    fun signup(request: SignUpRequest)

    fun login(request: LoginRequest)
}