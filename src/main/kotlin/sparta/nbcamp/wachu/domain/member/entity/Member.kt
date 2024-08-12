package sparta.nbcamp.wachu.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service

@Entity
@Table(name = "member")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    fun changePassword(password: String, passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(password)
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changeProfileImageUrl(multipartFile: MultipartFile, mediaS3Service: MediaS3Service) {
        this.profileImageUrl = mediaS3Service.upload(multipartFile, S3FilePath.PROFILE.path)
    }
}