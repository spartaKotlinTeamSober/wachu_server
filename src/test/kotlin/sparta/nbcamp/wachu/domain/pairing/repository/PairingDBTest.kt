package sparta.nbcamp.wachu.domain.pairing.repository

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingJpaRepository
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingQueryDslRepository
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository

@SpringBootTest
class PairingDBTest {
    @Autowired
    private lateinit var wineJpaRepository: WineJpaRepository

    @Autowired
    private lateinit var pairingJpaRepository: PairingJpaRepository

    @Autowired
    private lateinit var pairingQueryDslRepository: PairingQueryDslRepository

    private val logger: Logger = LoggerFactory.getLogger(PairingDBTest::class.java)

    @BeforeEach
    fun `와인 10개, 각 와인당 페어링 100개 세팅한다`() {
        val testWines = List(10) { index ->
            Wine(
                name = "testWine$index",
                sweetness = 0,
                acidity = 0,
                body = 0,
                tannin = 0,
                wineType = WineType.RED,
                aroma = "test",
                price = 0,
                kind = "test",
                style = "test",
                country = "test",
                region = "test",
                embedding = "test"
            )
        }
        testWines.count() shouldBe 10
        testWines.forEach { wine ->
            val testPairings = List(100) { index ->
                Pairing(
                    wine = wine,
                    memberId = index.toLong(),
                    title = "test$index",
                    description = "testDescription$index",
                    photoUrl = "testPhoto$index$"
                )
            }
            testPairings.count() shouldBe 100
        }
    }

    @Test
    fun `fetchjoin 전 테스트`() {

        val startTime = System.currentTimeMillis()

        pairingJpaRepository.findByWineId(1L, PageRequest.of(0, 10))

        val endTime = System.currentTimeMillis()

        val noFetchJoinResult = endTime - startTime

        logger.info("fetch join 을 하지 않고 걸린시간: $noFetchJoinResult")
    }

    @Test
    fun `fetchjoin 후 테스트`() {

        val startTime2 = System.currentTimeMillis()

        pairingQueryDslRepository.findFetchJoin(1L, PageRequest.of(0, 10))

        val endTime2 = System.currentTimeMillis()

        val fetchJoinResult = endTime2 - startTime2

        logger.info("fetch join 을 하고 걸린시간: $fetchJoinResult")
    }
}