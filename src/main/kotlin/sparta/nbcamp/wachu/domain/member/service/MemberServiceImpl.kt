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
import sparta.nbcamp.wachu.domain.member.dto.UpdateRequest
import sparta.nbcamp.wachu.domain.member.emailcode.dto.SendCodeRequest
import sparta.nbcamp.wachu.domain.member.emailcode.service.CodeService
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse
import java.util.UUID

@Service
class MemberServiceImpl @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenManager: JwtTokenManager,
    private val mediaS3Service: MediaS3Service,
    private val codeService: CodeService,
) : MemberService {

    override fun sendValidationCode(request: SendCodeRequest) {
        check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
        codeService.sendCode(request.email)
    }

    override fun signup(request: SignUpRequest, multipartFile: MultipartFile?): SignUpResponse {
        check(codeService.checkCode(request.email, request.code)) { "인증코드가 맞지 않음" }
        check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
        check(request.password == request.confirmPassword) { "처음에 설정한 비밀번호와 다름" }
        check(!memberRepository.existsByNickname(request.nickname)) { "이미 존재하는 닉네임" }
        val member = SignUpRequest.toEntity(request, passwordEncoder)
        memberRepository.addMember(member)
        multipartFile?.let {
            val profileUrl = mediaS3Service.upload(multipartFile, S3FilePath.PROFILE.path)
            member.profileImageUrl = profileUrl
        }
        return SignUpResponse.from(member)
    }

    override fun socialLogin(request: OAuthResponse): TokenResponse {
        val member =
            memberRepository.findByProviderAndProviderId(
                provider = request.provider,
                providerId = request.providerId
            )
        if (member == null) {
            if (request.email != null && memberRepository.existsByEmail(request.email!!)) request.email = null
            if (memberRepository.existsByNickname(request.nickname!!)) request.nickname = null

            memberRepository.addMember(
                Member(
                    email = request.email,
                    nickname = request.nickname,
                    password = passwordEncoder.encode(UUID.randomUUID().toString()),
                    profileImageUrl = request.profileImageUrl,
                    provider = request.provider,
                    providerId = request.providerId,
                )
            ).let {
                val memberId = it.id!!
                val token = jwtTokenManager.generateTokenResponse(memberId = memberId, memberRole = MemberRole.MEMBER)
                return token
            }
        } else {
            val tokens = jwtTokenManager.generateTokenResponse(memberId = member.id!!, memberRole = MemberRole.MEMBER)
            return tokens
        }
    }

    override fun login(request: LoginRequest): TokenResponse {

        val loginMember = memberRepository.findByEmail(request.email) ?: throw IllegalStateException("이메일이 없음")
        check(
            passwordEncoder.matches(
                request.password,
                loginMember.password
            )
        ) { "비밀번호가 맞지 않음" }
        val tokens = jwtTokenManager.generateTokenResponse(loginMember.id!!, MemberRole.MEMBER)

        return tokens
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

    @Transactional
    override fun updateProfile(
        userPrincipal: UserPrincipal,
        request: UpdateRequest,
        multipartFile: MultipartFile?,
    ): SignUpResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        request.email?.let {
            check(!memberRepository.existsByEmail(request.email)) { "존재하는 이메일" }
            check(request.email != member.email) { "기존과 동일한 이메일" }
            codeService.sendCode(request.email)
        }
        request.password?.let {
            check(request.password == request.confirmPassword) { "설정한 비밀번호와 다름" }
            member.password = passwordEncoder.encode(it)
        }
        request.nickname?.let { member.nickname = it }
        multipartFile?.let {
            val profileUrl = mediaS3Service.upload(multipartFile, S3FilePath.PROFILE.path)
            member.profileImageUrl = profileUrl
        }
        return SignUpResponse.from(member)
    }

    override fun confirmUpdateEmail(
        userPrincipal: UserPrincipal,
        request: SendCodeRequest,
        code: String,
    ): SignUpResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        check(codeService.checkCode(request.email, code)) { "인증코드가 맞지 않음" }
        member.email = request.email
        return SignUpResponse.from(member)
    }

    override fun refreshAccessToken(refreshToken: String): TokenResponse {
        return jwtTokenManager.validateToken(refreshToken).fold(
            onSuccess = {
                val tokens = jwtTokenManager.generateTokenResponse(
                    memberId = it.payload.subject.toLong(),
                    memberRole = MemberRole.valueOf(it.payload.get("memberRole", String::class.java))
                )
                tokens
            },
            onFailure = { throw IllegalStateException(" 토큰이 검증되지않음") }
        )
    }
}
