package sparta.nbcamp.wachu.domain.member.repository

import sparta.nbcamp.wachu.domain.member.entity.Member

interface MemberRepository {

    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByEmail(email: String): Member?
    fun addMember(member: Member): Member
}