package sparta.nbcamp.wachu.domain.member.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.ProfileResponse
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.emailcode.dto.SendCodeRequest
import sparta.nbcamp.wachu.domain.member.emailcode.service.CodeService
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Service
class MemberServiceImpl @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenManager: JwtTokenManager,
    private val mediaS3Service: MediaS3Service
    private val codeService: CodeService,
) : MemberService {

    override fun sendValidationCode(request: SendCodeRequest) {
        check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
        codeService.sendCode(request.email)
    }

    override fun signup(request: SignUpRequest): SignUpResponse {
        check(codeService.checkCode(request.email, request.code)) { "인증코드가 맞지 않음" }
        check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
        check(request.password == request.confirmPassword) { "처음에 설정한 비밀번호와 다름" }
        check(!memberRepository.existsByNickname(request.nickname)) { "이미 존재하는 닉네임" }

        val member = SignUpRequest.toEntity(request, passwordEncoder)
        memberRepository.addMember(member)
        return SignUpResponse.from(member)
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
            accessToken = jwtTokenManager.generateToken(memberId = loginMember.id!!, memberRole = MemberRole.MEMBER),
            refreshToken = null
        )
    }

    @Transactional
    override fun uploadProfile(userPrincipal: UserPrincipal, multipartFile: MultipartFile): ProfileResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        val profileUrl = mediaS3Service.upload(multipartFile, S3FilePath.PROFILE.path)
        member.profileImageUrl = profileUrl
        return ProfileResponse.from(member)
    }

    override fun getProfile(userPrincipal: UserPrincipal): ProfileResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        return ProfileResponse.from(member)
    }
}