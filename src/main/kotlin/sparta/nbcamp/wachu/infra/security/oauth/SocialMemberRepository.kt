package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMemberRepository: JpaRepository<SocialMember, Long> {
    fun findByProviderNameAndProviderId(providerName: String, providerId:String): SocialMember?
}