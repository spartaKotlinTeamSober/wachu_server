package sparta.nbcamp.wachu.domain.member.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.dto.toEntity
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.entity.toSignUpResponse
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager

@Service
class MemberServiceImpl @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenManager: JwtTokenManager,
) : MemberService {

    override fun signup(request: SignUpRequest): SignUpResponse {
        check(!memberRepository.existsByEmail(request.email)) { "이미 존재하는 이메일입니다." }
        check(request.password == request.confirmPassword) { "처음에 설정한 비밀번호와 다릅니다." }
        check(!memberRepository.existsByNickname(request.nickname)) { "이미 존재하는 닉네임입니다." }

        val member = memberRepository.addMember(request.toEntity(passwordEncoder))
        return member.toSignUpResponse()
    }

    override fun login(request: LoginRequest): TokenResponse {

        val loginMember = memberRepository.findByEmail(request.email) ?: throw RuntimeException("잘못된 아이디")
        check(
            passwordEncoder.matches(
                request.password,
                loginMember.password
            )
        ) { "비밀번호가 맞지 않음" }
        return TokenResponse(
            accessToken = jwtTokenManager.generateToken(memberId = loginMember.id, memberRole = MemberRole.MEMBER),
            refreshToken = null
        )
    }
}