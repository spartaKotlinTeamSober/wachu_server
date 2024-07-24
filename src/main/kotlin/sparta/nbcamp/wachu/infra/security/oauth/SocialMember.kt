package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class SocialMember (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_member_id")
    var id:Long? =null,

    var providerName: String,// 카카오,네이버 등등등
    var providerId: String,
    var nickname: String,
)