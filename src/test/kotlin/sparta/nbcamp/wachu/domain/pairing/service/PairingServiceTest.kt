package sparta.nbcamp.wachu.domain.pairing.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.pairing.dto.v1.PairingRequest
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.PairingTestRepositoryImpl
import sparta.nbcamp.wachu.domain.pairing.service.v1.PairingServiceImpl
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.media.MediaService
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

class PairingServiceTest {

    val defaultWine = Wine(
        id = 1L,
        name = "testWine",
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

    val defaultPairing = Pairing(
        wine = defaultWine,
        memberId = 1L,
        title = "test",
        description = "test",
        photoUrl = "test"
    ).apply { id = 1L }

    val defaultPairingList = List(10) { index ->
        Pairing(
            wine = defaultWine,
            memberId = index.toLong(),
            title = "test$index",
            description = "testDescription$index",
            photoUrl = "testPhoto$index$"
        ).apply { id = index.toLong() }
    }

    val defaultPageable = PageRequest.of(0, 10)
    val defaultPairingPage = PageImpl(defaultPairingList, defaultPageable, defaultPairingList.size.toLong())

    val wineRepository: WineRepository = mockk()
    val memberRepository: MemberRepository = mockk()
    val pairingRepository = PairingTestRepositoryImpl(defaultPairing, defaultPairingPage)
    val mediaService: MediaService = mockk()
    val pairingService = PairingServiceImpl(wineRepository, memberRepository, pairingRepository, mediaService)

    @Test
    fun `존재하는 아이디로 getPairing하면 PairingResponseDto를 반환한다`() {

        val responseDto = pairingService.getPairing(1L)
        val wineResponseDto = WineResponse.from(defaultWine)

        responseDto.id shouldBe defaultPairing.id
        responseDto.title shouldBe defaultPairing.title
        responseDto.memberId shouldBe defaultPairing.memberId
        responseDto.wine shouldBe wineResponseDto
        responseDto.photoUrl shouldBe defaultPairing.photoUrl
        responseDto.createdAt shouldBe defaultPairing.createdAt
        responseDto.description shouldBe defaultPairing.description
    }

    @Test
    fun `존재하지 않는 아이디로 getPairing하면 ModelNotFoundException이 발생한다`() {
        shouldThrow<ModelNotFoundException> {
            pairingService.getPairing(0L)
        }
    }

    @Test
    fun `getPairingPage 하면 pairing 페이지를 반환한다`() {
        val result = pairingService.getPairingPage(defaultPageable)
        result.size shouldBe defaultPairingPage.size
        result.forEachIndexed { index, response ->
            response.title shouldBe defaultPairingPage.content[index].title
        }
    }

    @Test
    fun `createPairing 하면 새로운 Pairing이 생성된다`() {
        val pairingCreateRequest = PairingRequest(
            wineId = 1L,
            title = "newTest",
            description = "newTest",
        )
        val testWine = defaultWine
        every { wineRepository.findByIdOrNull(1L) } returns testWine
        val testWineResponse = WineResponse.from(testWine)
        val testUserPrincipal = UserPrincipal(memberId = 1L, memberRole = setOf("MEMBER"))
        every { memberRepository.findById(any()) } returns Member(
            "test", "test", "test", "test"
        ).apply { id = testUserPrincipal.memberId }

        val image = mockk<MultipartFile>()
        val imageUrl = "test"
        every { mediaService.upload(image, any()) } returns imageUrl
        val response = pairingService.createPairing(testUserPrincipal, pairingCreateRequest, image)

        response.title shouldBe pairingCreateRequest.title
        response.memberId shouldBe testUserPrincipal.memberId
        response.wine shouldBe testWineResponse
        response.description shouldBe pairingCreateRequest.description
    }

    @Test
    fun `deletePairing 했을 때 권한이 없으면 AccessDeniedException이 발생한다`() {
        val testUserPrincipal = UserPrincipal(memberId = 2L, memberRole = setOf("MEMBER"))

        shouldThrow<AccessDeniedException> {
            pairingService.deletePairing(testUserPrincipal, 1L)
        }
    }

    @Test
    fun `Admin이 deletePairing 을 사용해 삭제하면 삭제에 성공한다`() {
        val testAdminPrincipal = UserPrincipal(memberId = 0L, memberRole = setOf("ADMIN"))

        pairingService.deletePairing(testAdminPrincipal, 1L)
    }
}

