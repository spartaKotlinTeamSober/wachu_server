package sparta.nbcamp.wachu.domain.member.service

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.dto.*
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

interface MemberService {
    fun signup(request: SignUpRequest): SignUpResponse

    fun login(request: LoginRequest): TokenResponse

    fun uploadProfile(userPrincipal: UserPrincipal, multipartFile: MultipartFile): ProfileResponse

    fun getProfile(userPrincipal: UserPrincipal): ProfileResponse
}