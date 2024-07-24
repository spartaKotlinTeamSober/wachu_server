package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager

@Service
class KakaoOAuth2LoginService(
    private val kakaoOAuth2LoginClient: KakaoOAuth2LoginClient,
    private val socialMemberDomainService: SocialMemberDomainService,
    private val jwtTokenManager: JwtTokenManager,
) {

    fun generateLoginPageUrl():String{
        // 로그인 페이지에 대한 URL 생성 및 반환
        return kakaoOAuth2LoginClient.generateLoginPageUrl()

    }

    fun login(code: String):String{

        // CODE를 통해서 토큰 발급
        return kakaoOAuth2LoginClient.getAccessToken(code)
        // 토큰을 통해서 사용자정보 조회
            .let{ kakaoOAuth2LoginClient.retrieveUserInfo(it) }
        // 사용자 정보로 우리쪽 회원가입& 조회 (registerIfAbsent)
            .let{ socialMemberDomainService.registerIfAbsent(it)}
        // 우리쪽 토큰 만들어서 반환
            .let{ jwtTokenManager.generateToken(memberId = it.id!!, memberRole = MemberRole.MEMBER)}
    }
}