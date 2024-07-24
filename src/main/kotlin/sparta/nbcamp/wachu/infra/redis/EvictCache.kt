package sparta.nbcamp.wachu.infra.redis

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class EvictCache(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun evictCaches(deleteCache: String) {
        val keys = redisTemplate.keys("$deleteCache::*")
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
            logger.info("삭제된 키들: $keys")
        } else {
            logger.info("삭제할 키가 없습니다.")
        }
    }
}