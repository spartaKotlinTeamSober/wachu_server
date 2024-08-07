package sparta.nbcamp.wachu.domain.pairing.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberJpaRepository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingJpaRepository
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingQueryDslRepository
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingQueryDslRepositoryImpl
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewJpaRepository
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PairingQueryDslRepositoryImpl::class)
class PairingRepositoryTest {
    @Autowired
    private lateinit var reviewJpaRepository: ReviewJpaRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    private lateinit var wineJpaRepository: WineJpaRepository

    @Autowired
    private lateinit var pairingJpaRepository: PairingJpaRepository

    @Autowired
    private lateinit var pairingQueryDslRepository: PairingQueryDslRepository

    private val logger: Logger = LoggerFactory.getLogger(PairingRepositoryTest::class.java)

    @BeforeEach
    fun setUp() {
        reviewJpaRepository.deleteAll()
        memberJpaRepository.deleteAll()
        wineJpaRepository.deleteAll()
        pairingJpaRepository.deleteAll()
        val testMembers = List(10) { index ->
            Member(
                email = "test$index",
                password = "test$index",
                nickname = "test$index",
                profileImageUrl = "test$index",
                memberRole = MemberRole.MEMBER,
            )
        }
        memberJpaRepository.saveAll(testMembers)

        val savedMembers = memberJpaRepository.findAll()

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
        wineJpaRepository.saveAll(testWines)

        val testPairings = savedMembers.flatMap { member ->
            testWines.flatMap { wine ->
                List(10) { index ->
                    Pairing(
                        wine = wine,
                        memberId = member.id!!,
                        title = "test$index",
                        description = "testDescription$index",
                        photoUrl = "testPhoto$index$"
                    )
                }
            }
        }
        pairingJpaRepository.saveAll(testPairings)
    }

    @Test
    fun `페치조인 안한다`() {
        val startTime = System.currentTimeMillis()

        pairingJpaRepository.findByWineId(1L, PageRequest.of(0, 10))

        val endTime = System.currentTimeMillis()

        val noFetchJoinResult = endTime - startTime

        logger.info("fetch join 을 하지 않고 걸린시간: $noFetchJoinResult")
    }

    @Test
    fun `페치조인 한다`() {
        val startTime2 = System.currentTimeMillis()

        pairingQueryDslRepository.findFetchJoin(1L, PageRequest.of(0, 10))

        val endTime2 = System.currentTimeMillis()

        val fetchJoinResult = endTime2 - startTime2

        logger.info("fetch join 을 하고 걸린시간: $fetchJoinResult")
    }
}
