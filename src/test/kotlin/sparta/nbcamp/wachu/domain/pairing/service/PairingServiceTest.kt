package sparta.nbcamp.wachu.domain.pairing.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.jupiter.api.Test
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepository
import sparta.nbcamp.wachu.domain.pairing.service.v1.PairingServiceImpl
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal
import java.util.logging.Level
import java.util.logging.Logger

class PairingServiceTest : BehaviorSpec({
    val defaultPairing = Pairing(
        wineId = 1L,
        memberId = 1L,
        title = "test",
        description = "test",
        photoUrl = "test"
    )

    val defaultPairingList = List(10) { index ->
        Pairing(
            wineId = index.toLong(),
            memberId = index.toLong(),
            title = "test$index",
            description = "testDescription$index",
            photoUrl = "testPhoto$index$"
        )
    }

    val memberRepository: MemberRepository = mockk()
    val pairingRepository = PairingTestRepositoryImpl(defaultPairing, defaultPairingList)

    val pairingService = PairingServiceImpl(memberRepository, pairingRepository)

    afterContainer {
        clearAllMocks()
    }

    @Test
    fun test() =
        given("ParingService 에서") {
            `when`("존재하는 아이디로 getPairing 하면") {
                val responseDto = pairingService.getPairing(1L)

                then("PairingResponseDto를 반환한다") {
                    responseDto.id shouldBe defaultPairing.id
                    responseDto.title shouldBe defaultPairing.title
                    responseDto.memberId shouldBe defaultPairing.memberId
                    responseDto.wineId shouldBe defaultPairing.wineId
                    responseDto.photoUrl shouldBe defaultPairing.photoUrl
                    responseDto.createdAt shouldBe defaultPairing.createdAt
                    responseDto.description shouldBe defaultPairing.description
                }
            }

            `when`("존재하지 않는 아이디로 getPairing 하면") {
                then("ModelNotFoundException이 발생한다") {
                    shouldThrow<ModelNotFoundException> {
                        pairingService.getPairing(0L)
                    }
                }
            }

            `when`("getPairingList 하면") {
                val result = pairingService.getPairingList()

                then("pairing 목록을 반환한다") {
                    result.size shouldBe defaultPairingList.size
                    result.forEachIndexed { index, response ->
                        response.title shouldBe defaultPairingList[index].title
                    }
                }
            }

            val pairingCreateRequest = PairingRequest(
                wineId = 1L,
                title = "newTest",
                description = "newTest",
                photo = "newTest"
            )

            val testUserPrincipal = UserPrincipal(memberId = 1L, memberRole = setOf("ROLE_MEMBER"))
            val testUserPrincipal2 = UserPrincipal(memberId = 2L, memberRole = setOf("ROLE_MEMBER"))
            val testAdminPrincipal = UserPrincipal(memberId = 0L, memberRole = setOf("ROLE_ADMIN"))

            `when`("createPairing 을 사용해 새로 생성하면") {
                val response = pairingService.createPairing(testUserPrincipal, pairingCreateRequest)

                then("새로운 Pairing에 대한 dto가 반환된다.") {
                    response.title shouldBe pairingCreateRequest.title
                    response.memberId shouldBe testUserPrincipal.memberId
                    response.wineId shouldBe pairingCreateRequest.wineId
                    response.photoUrl shouldBe pairingCreateRequest.photo
                    response.description shouldBe pairingCreateRequest.description
                }
            }

            `when`("Admin이 deletePairing 을 사용해 삭제하면") {
                then("삭제 성공한다.") {
                    pairingService.deletePairing(testAdminPrincipal, 1L)
                }
            }

            `when`("deletePairing 을 사용해 삭제하면") {
                then("권한이 없다면 AccessDeniedException이 발생한다.") {
                    shouldThrow<AccessDeniedException> {
                        pairingService.deletePairing(testUserPrincipal2, 1L)
                    }
                }
            }
        }
})

class PairingTestRepositoryImpl(
    private val pairing: Pairing,
    private val pairingList: List<Pairing>
) : PairingRepository {
    override fun findById(id: Long): Pairing? {
        if (id !in 1..10) return null
        return pairing
    }

    override fun findAll(): List<Pairing> {
        return pairingList
    }

    override fun save(pairing: Pairing): Pairing {
        return pairing
    }

    override fun delete(pairing: Pairing) {
        Logger.getLogger("PairingTestRepositoryImpl").log(Level.INFO, "delete")
    }
}