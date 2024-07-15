package sparta.nbcamp.wachu.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.member.entity.Member

interface MemberJpaRepository : JpaRepository<Member, Long>