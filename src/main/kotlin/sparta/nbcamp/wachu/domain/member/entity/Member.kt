package sparta.nbcamp.wachu.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class Member(

    @Column(name = "email", length = 50, unique = true)
    val email: String,

    @Column(name = "password", length = 60)
    val password: String,

    @Column(name = "nickname", length = 50, unique = true)
    val nickname: String,

    @Column(name = "profile_image_url")
    val profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val memberRole: MemberRole = MemberRole.MEMBER,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)