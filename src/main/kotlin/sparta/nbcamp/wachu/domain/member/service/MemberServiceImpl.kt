package sparta.nbcamp.wachu.domain.member.service

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest

@Service
@RequestMapping("/auth")
class MemberServiceImpl : MemberService {
    override fun signup(request: SignUpRequest) {
        TODO("Not yet implemented")
    }

    override fun login(request: LoginRequest) {
        TODO("Not yet implemented")
    }
}