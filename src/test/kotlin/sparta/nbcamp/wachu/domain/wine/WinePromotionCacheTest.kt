package sparta.nbcamp.wachu.domain.wine

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.RedisTemplate
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.repository.WinePromotionRepository
import sparta.nbcamp.wachu.domain.wine.service.WineService
import sparta.nbcamp.wachu.infra.redis.EvictCache

@SpringBootTest
//@ActiveProfiles("test")
class WinePromotionCacheTest {

    @Autowired
    private lateinit var wineService: WineService

    @Autowired
    private lateinit var evictCache: EvictCache

    @SpyBean
    private lateinit var winePromotionRepository: WinePromotionRepository

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `캐시 적용여부 테스트`() {

        evictCache.evictCaches(deleteCache = "promotionCache")

        val firstCall: Page<PromotionWineResponse> = wineService.getPromotionWineList(
            page = 0,
            size = 6,
            sortBy = "createdAt",
            direction = "asc"
        )
        assertNotNull(firstCall)

        verify(winePromotionRepository).findPromotionWineList(
            PageRequest.of(
                0,
                6,
                Sort.by(Sort.Direction.ASC, "createdAt")
            )
        )

        val secondCall: Page<PromotionWineResponse> = wineService.getPromotionWineList(
            page = 0,
            size = 6,
            sortBy = "createdAt",
            direction = "asc"
        )
        assertNotNull(secondCall)

        // 두 번째 호출 시 데이터베이스 쿼리가 발생하지 않는지 확인
        verify(winePromotionRepository, Mockito.times(1)).findPromotionWineList(
            PageRequest.of(
                0,
                6,
                Sort.by(Sort.Direction.ASC, "createdAt")
            )
        )
        evictCache.evictCaches(deleteCache = "promotionCache")
    }

    @Test
    fun `admin이 프로모션 추가할때 캐시 삭제 확인 테스트`() {

        val firstCall: Page<PromotionWineResponse> = wineService.getPromotionWineList(
            page = 0,
            size = 6,
            sortBy = "createdAt",
            direction = "asc"
        )
        assertNotNull(firstCall)
        // 캐시 삭제
        evictCache.evictCaches(deleteCache = "promotionCache")

        // 캐시가 삭제되었는지 테스트
        val keys = redisTemplate.keys("promotionCache*")
        assertTrue(keys.isEmpty())
    }
}