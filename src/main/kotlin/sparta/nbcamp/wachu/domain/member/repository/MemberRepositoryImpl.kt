package sparta.nbcamp.wachu.domain.member.repository

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.member.entity.Member

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {
    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email)
    }

    override fun existsByNickname(nickname: String): Boolean {
        return memberJpaRepository.existsByNickname(nickname)
    }

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)
    }

    override fun addMember(member: Member): Member {
        return memberJpaRepository.save(member)
    }

    override fun findById(id: Long): Member? {
        return memberJpaRepository.findByIdOrNull(id)
    }

    override fun findByProviderAndProviderId(provider: String, providerId: String): Member? {
        return memberJpaRepository.findByProviderAndProviderId(provider, providerId)
    }
}