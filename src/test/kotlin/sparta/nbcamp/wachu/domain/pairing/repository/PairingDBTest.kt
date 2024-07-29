package sparta.nbcamp.wachu.domain.pairing.repository

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.wine.WineTestRepositoryImpl
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType

class PairingDBTest {

    private val wineRepository: WineTestRepositoryImpl = mockk()
    private val pairingRepository: PairingTestRepositoryImpl = mockk()

    private val logger: Logger = LoggerFactory.getLogger(PairingDBTest::class.java)

    @BeforeEach
    fun setUp() {
        wineRepository.deleteAllforTest()
        pairingRepository.deleteAllforTest()
        val wines = List(10) { index ->
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
        wineRepository.saveAllforTest(wines)
        wines.forEach { wine ->
            val pairings = List(1000) { index ->
                Pairing(
                    wine = wine,
                    memberId = index.toLong(),
                    title = "test$index",
                    description = "testDescription$index",
                    photoUrl = "testPhoto$index$"
                )
            }
            pairingRepository.saveAllforTest(pairings)
        }
    }
}