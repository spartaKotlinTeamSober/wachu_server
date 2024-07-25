package sparta.nbcamp.wachu.domain.member.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager

@Service
class MemberServiceImpl @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenManager: JwtTokenManager,
) : MemberService {

    override fun signup(request: SignUpRequest): SignUpResponse {
        check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
        check(request.password == request.confirmPassword) { "처음에 설정한 비밀번호와 다름" }
        check(!memberRepository.existsByNickname(request.nickname)) { "이미 존재하는 닉네임" }

        val member = SignUpRequest.toEntity(request, passwordEncoder)
        memberRepository.addMember(member)
        return SignUpResponse.from(member)
    }

    override fun login(request: LoginRequest): TokenResponse {

        val loginMember = memberRepository.findByEmail(request.email) ?: throw IllegalStateException("이메일이 없음")
        check(
            passwordEncoder.matches(
                request.password,
                loginMember.password
            )
        ) { "비밀번호가 맞지 않음" }
        return TokenResponse(
            accessToken = jwtTokenManager.generateToken(memberId = loginMember.id!!, memberRole = MemberRole.MEMBER),
            refreshToken = null
        )
    }
}