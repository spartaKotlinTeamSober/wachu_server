package sparta.nbcamp.wachu.domain.wine

import io.kotest.matchers.longs.shouldBeLessThan
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.wine.repository.WinePromotionRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineQueryDslRepository

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class WinePromotionDBTest {

    @Autowired
    private lateinit var wineQueryDslRepository: WineQueryDslRepository

    @Autowired
    private lateinit var winePromotionRepository: WinePromotionRepository

    private val logger: Logger = LoggerFactory.getLogger(WinePromotionDBTest::class.java)

    @Test
    fun `fetch join 전 후 시간이 빨라졌는지 확인하는  테스트`() {

        val page = 0
        val size = 10
        val direction = "desc"
        val sortBy = "closedAt"

        val pageable: Pageable = PageRequest.of(page, size, getDirection(direction), sortBy)

        val startTime = System.currentTimeMillis()
        wineQueryDslRepository.findAllWithoutFetchJoinForTest(pageable = pageable)
        val endTime = System.currentTimeMillis()

        val noFetchJoinResult = endTime - startTime

        val startTime2 = System.currentTimeMillis()
        winePromotionRepository.findPromotionWineList(pageable = pageable)
        val endTime2 = System.currentTimeMillis()

        val fetchJoinResult = endTime2 - startTime2

        logger.info("fetch join 을 하지 않고 걸린시간: $noFetchJoinResult")
        logger.info("fetch join 을 하고 걸린시간: $fetchJoinResult")

        (fetchJoinResult - noFetchJoinResult) shouldBeLessThan 0
    }

    private fun getDirection(sort: String) = when (sort.lowercase()) {
        "asc" -> Sort.Direction.ASC
        else -> Sort.Direction.DESC
    }
}