package sparta.nbcamp.wachu.domain.review.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMediaType
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia
import sparta.nbcamp.wachu.domain.review.repository.ReviewMultiMediaTestRepository
import sparta.nbcamp.wachu.domain.review.repository.ReviewTestRepositoryImpl
import sparta.nbcamp.wachu.domain.review.service.v1.ReviewServiceImpl
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.domain.wine.service.WineImageGetter
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

class ReviewServiceTest {

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

    val defaultMember = Member(
        "test", "test", "test", "test"
    ).apply { id = 1L }

    val defaultReview = Review(
        wine = defaultWine,
        memberId = defaultMember.id!!,
        title = "test",
        description = "test",
        score = 1.5
    ).apply { id = 1L }

    val defaultReviewList = List(10) { index ->
        Review(
            wine = defaultWine,
            memberId = defaultMember.id!!,
            title = "test$index",
            description = "testDescription$index",
            score = index.toDouble()
        ).apply { id = index.toLong() }
    }

    val defaultReviewMultiMediaList = List(10) { index ->
        ReviewMultiMedia(
            reviewId = index.toLong(),
            mediaUrl = "test$index",
            mediaType = ReviewMediaType.IMAGE,
        ).apply { id = index.toLong() }
    }
    val defaultPageable = PageRequest.of(0, 10)
    val defaultReviewPage = PageImpl(defaultReviewList, defaultPageable, defaultReviewList.size.toLong())

    val wineRepository: WineRepository = mockk()
    val memberRepository: MemberRepository = mockk()
    val mediaService: MediaS3Service = mockk()
    val reviewRepository =
        ReviewTestRepositoryImpl(defaultReview, defaultReviewPage)
    val reviewMultiMediaRepository = ReviewMultiMediaTestRepository(defaultReviewMultiMediaList)

    val reviewService = ReviewServiceImpl(
        wineRepository = wineRepository,
        memberRepository = memberRepository,
        reviewRepository = reviewRepository,
        reviewMediaRepository = reviewMultiMediaRepository,
        mediaS3Service = mediaService
    )

    @BeforeEach
    fun setup() {
        mockkObject(WineImageGetter)
        WineImageGetter.init(mediaService)
        every { WineImageGetter.getWineImage(any(), any()) } returns "testUrl1"
    }

    @Test
    fun `존재하는 아이디로 getReview하면 ReviewResponseDto를 반환한다`() {
        every { memberRepository.findById(any()) } returns defaultMember
        val responseDto = reviewService.getReview(1L)
        val wineResponseDto = WineResponse.from(defaultWine)

        responseDto.id shouldBe defaultReview.id
        responseDto.title shouldBe defaultReview.title
        responseDto.member.id shouldBe defaultMember.id
        responseDto.wine shouldBe wineResponseDto
        responseDto.score shouldBe defaultReview.score
        responseDto.createdAt shouldBe defaultReview.createdAt
        responseDto.description shouldBe defaultReview.description
    }

    @Test
    fun `존재하지 않는 아이디로 getReview하면 ModelNotFoundException이 발생한다`() {
        shouldThrow<ModelNotFoundException> {
            reviewService.getReview(0L)
        }
    }

    @Test
    fun `getReviewPage 하면 review 페이지를 반환한다`() {
        every { memberRepository.findAllById(any()) } returns listOf(defaultMember)
        val result = reviewService.getReviewPage(defaultPageable)

        result.size shouldBe defaultReviewPage.size
        result.forEachIndexed { index, response ->
            response.title shouldBe defaultReviewPage.content[index].title
        }
    }

    @Test
    fun `createReview 하면 review 가 생성된다`() {
        val reviewCreateRequest = ReviewRequest(
            wineId = 1L,
            title = "newtest",
            description = "newtest",
            score = 1.5
        )
        val testWine = defaultWine
        every { wineRepository.findByIdOrNull(1L) } returns testWine
        val testWineResponse = WineResponse.from(testWine)

        val testUserPrincipal = UserPrincipal(memberId = defaultMember.id!!, memberRole = setOf("MEMBER"))
        every { memberRepository.findById(any()) } returns defaultMember

        val response = reviewService.createReview(testUserPrincipal, reviewCreateRequest, null)

        response.title shouldBe reviewCreateRequest.title
        response.member.id shouldBe testUserPrincipal.memberId
        response.wine shouldBe testWineResponse
        response.score shouldBe reviewCreateRequest.score
        response.description shouldBe reviewCreateRequest.description
    }

    @Test
    fun `deleteReview 했을 때 권한이 없으면 AccessDeniedException이 발생한다`() {
        val testUserPrincipal = UserPrincipal(memberId = 2L, memberRole = setOf("MEMBER"))

        shouldThrow<AccessDeniedException> {
            reviewService.deleteReview(testUserPrincipal, 1L)
        }
    }

    @Test
    fun `Admin이 deleteReview 을 사용해 삭제하면 삭제에 성공한다`() {
        val testAdminPrincipal = UserPrincipal(memberId = 0L, memberRole = setOf("ADMIN"))

        reviewService.deleteReview(testAdminPrincipal, 1L)
    }

    @Test
    fun `createReviewMedia를 하면 ReviewMultuMedeia가 생성된다`() {
        val testUserPrincipal = UserPrincipal(memberId = 1L, memberRole = setOf("MEMBER"))
        every { memberRepository.findById(any()) } returns Member(
            "test", "test", "test", "test"
        ).apply { id = testUserPrincipal.memberId }

        val multiMedia = List(10) {
            mockk<MultipartFile>(relaxed = true)
            {
                every { contentType } returns "image/jpeg"
            }
        }

        val mediaUrls = List(10) { "url$it" }

        every { mediaService.upload(eq(multiMedia), eq(S3FilePath.REVIEW.path + "1/")) } returns mediaUrls

        val responseMedia = reviewService.createReviewMedia(testUserPrincipal, 1L, multiMedia)

        responseMedia.size shouldBe 10
        responseMedia.first().mediaUrl shouldBe "test0"
    }
}