package sparta.nbcamp.wachu.domain.member.repository

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
}