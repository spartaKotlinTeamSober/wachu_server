package sparta.nbcamp.wachu.domain.testH2db

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberJpaRepository

@DataJpaTest
@ActiveProfiles("test")
class H2DatabaseConnectionTest {

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Test
    fun `H2 데이터베이스 연결 확인`() {
        val member = Member(
            email = "test@test.com",
            password = "password",
            nickname = "testuser",
            profileImageUrl = "profile.png",
            memberRole = MemberRole.MEMBER
        )

        memberJpaRepository.save(member)

        val foundMember = memberJpaRepository.findByIdOrNull(member.id)

        member.email shouldBe foundMember?.email
    }
}