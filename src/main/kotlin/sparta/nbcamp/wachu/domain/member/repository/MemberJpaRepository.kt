package sparta.nbcamp.wachu.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.member.entity.Member

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByEmail(email: String): Member?
}