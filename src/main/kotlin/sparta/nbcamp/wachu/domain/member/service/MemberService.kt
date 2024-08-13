package sparta.nbcamp.wachu.domain.member.service

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.dto.LoginRequest
import sparta.nbcamp.wachu.domain.member.dto.ProfileResponse
import sparta.nbcamp.wachu.domain.member.dto.ProfileUpdateRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpRequest
import sparta.nbcamp.wachu.domain.member.dto.SignUpResponse
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.emailcode.dto.SendCodeRequest
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

interface MemberService {
    fun sendValidationCode(request: SendCodeRequest)

    fun signup(request: SignUpRequest, multipartFile: MultipartFile?): SignUpResponse

    fun login(request: LoginRequest): TokenResponse

    fun uploadProfile(userPrincipal: UserPrincipal, multipartFile: MultipartFile): ProfileResponse

    fun getProfile(userPrincipal: UserPrincipal): ProfileResponse

    fun sendVerificationCodeForEmailUpdate(userPrincipal: UserPrincipal, request: SendCodeRequest): SignUpResponse

    fun updateProfile(
        userPrincipal: UserPrincipal,
        request: ProfileUpdateRequest,
        multipartFile: MultipartFile?,
    ): SignUpResponse

    fun socialLogin(request: OAuthResponse): TokenResponse

    fun refreshAccessToken(refreshToken: String): TokenResponse
}