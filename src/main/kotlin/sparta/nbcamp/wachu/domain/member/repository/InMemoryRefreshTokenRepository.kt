// package sparta.nbcamp.wachu.domain.member.repository
//
// import org.springframework.stereotype.Repository
//
// @Repository
// class InMemoryRefreshTokenRepository : RefreshTokenRepository {
//     private val storage = mutableMapOf<String, Long>()
//
//     override fun save(memberId: Long, refreshToken: String) {
//         storage[refreshToken] = memberId
//     }
//
//     override fun delete(memberId: Long) {
//         storage.values.remove(memberId)
//     }
//
//     override fun findByToken(refreshToken: String): Long? {
//         return storage[refreshToken]
//     }
// }
