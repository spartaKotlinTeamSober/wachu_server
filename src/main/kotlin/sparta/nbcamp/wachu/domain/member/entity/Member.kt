package sparta.nbcamp.wachu.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(name = "member")
@SQLDelete(sql = "UPDATE member SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
class Member(

    @Column(name = "email", length = 50, unique = true)
    var email: String?,

    @Column(name = "password", length = 60)
    var password: String,

    @Column(name = "nickname", length = 50, unique = true)
    var nickname: String?,

    @Column(name = "profile_image_url")
    var profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val memberRole: MemberRole = MemberRole.MEMBER,

    @Column(name = "provider")
    val provider: String? = null,

    @Column(name = "provider_id")
    val providerId: String? = null,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    fun changeEmail(email: String) {
        this.email = email
    }

    fun changePassword(password: String, passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(password)
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changeProfileImageUrl(profileImageUrl: String) {
        this.profileImageUrl = profileImageUrl
    }
}