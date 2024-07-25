// package sparta.nbcamp.wachu.infra.security.oauth
//
// import jakarta.persistence.Column
// import jakarta.persistence.Entity
// import jakarta.persistence.GeneratedValue
// import jakarta.persistence.GenerationType
// import jakarta.persistence.Id
// import jakarta.persistence.Table
//
// @Entity
// @Table(name = "social_member")
// class SocialMember(
//
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "social_member_id")
//     var id: Long? = null,
//
//     @Column(name = "provider_name")
//     var providerName: String,// 카카오,네이버 등등등
//
//     @Column(name = "provider_id")
//     var providerId: String,
//
//     @Column(name = "nickname")
//     var nickname: String,
// )