package sparta.nbcamp.wachu.domain.member.entity

import jakarta.persistence.*

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
    var profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val memberRole: MemberRole = MemberRole.MEMBER,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)