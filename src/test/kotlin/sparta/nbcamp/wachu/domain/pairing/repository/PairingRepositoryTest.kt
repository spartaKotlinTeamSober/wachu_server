package sparta.nbcamp.wachu.domain.pairing.repository

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType

class PairingRepositoryTest {
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
    val testPairings = testWines.flatMap { wine ->
        List(100) { index ->
            Pairing(
                wine = wine,
                memberId = index.toLong(),
                title = "test$index",
                description = "testDescription$index",
                photoUrl = "testPhoto$index$"
            )
        }
    }
    val testPairing = testPairings.first()
    val testPairingsPage = PageImpl(testPairings, PageRequest.of(0, 10), testPairings.size.toLong())

    val pairingTestRepository = PairingTestRepositoryImpl(testPairing, testPairingsPage)

    private val logger: Logger = LoggerFactory.getLogger(PairingRepositoryTest::class.java)

    @Test
    fun `페치조인 안한다`() {
        val startTime = System.currentTimeMillis()

        val noFetchWine = testWines.first()
        val noFetchPairings = testPairings.filter { it.wine == noFetchWine }
        noFetchPairings.size shouldBe 100

        val endTime = System.currentTimeMillis()
        val noFetchJoinResult = endTime - startTime
        logger.info("fetch join 을 하지 않고 걸린시간: $noFetchJoinResult")
    }

    @Test
    fun `페치조인 한다`() {
        val startTime2 = System.currentTimeMillis()

        pairingTestRepository.findFetchJoin(testPairing.wine.id, PageRequest.of(0, 10))

        val endTime2 = System.currentTimeMillis()
        val fetchJoinResult = endTime2 - startTime2
        logger.info("fetch join 을 하고 걸린시간: $fetchJoinResult")
    }
}