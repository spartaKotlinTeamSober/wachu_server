package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.infra.security.oauth.dto.KakaoLoginUserInfoResponse

@Service
class SocialMemberDomainService(
    private val socialMemberRepository: SocialMemberRepository
) {
    fun registerIfAbsent(userInfo: KakaoLoginUserInfoResponse): SocialMember {
        // 일단 사용자 정보가 있는지 조회
        val socialMember: SocialMember? =
            socialMemberRepository.findByProviderNameAndProviderId("KAKAO", userInfo.id.toString())
        // 없으면 만들어서 반환
        return socialMember
            ?: socialMemberRepository.save(
                SocialMember(
                    providerName = "KAKAO",
                    providerId = userInfo.id.toString(),
                    nickname = userInfo.properties.nickname
                )
            )
    }
}