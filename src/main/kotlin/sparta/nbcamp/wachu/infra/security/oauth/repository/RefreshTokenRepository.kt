package sparta.nbcamp.wachu.infra.security.oauth.repository

interface RefreshTokenRepository {
    fun save(memberId: Long, refreshToken: String)
    fun delete(memberId: Long)
    fun findByToken(refreshToken: String): Long?
}