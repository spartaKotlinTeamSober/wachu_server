package sparta.nbcamp.wachu.domain.review.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMediaType
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia
import sparta.nbcamp.wachu.domain.review.repository.ReviewTestRepositoryImpl
import sparta.nbcamp.wachu.domain.review.service.v1.ReviewServiceImpl
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.S3Service
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

class ReviewServiceTest {
    val defaultReview = Review(
        wineId = 1L,
        memberId = 1L,
        title = "test",
        description = "test",
        score = 1.5
    ).apply { id = 1L }

    val defaultReviewList = List(10) { index ->
        Review(
            wineId = index.toLong(),
            memberId = index.toLong(),
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

    val memberRepository: MemberRepository = mockk()
    val s3Service: S3Service = mockk()
    val reviewRepository = ReviewTestRepositoryImpl(defaultReview, defaultReviewList,defaultReviewMultiMediaList)

    val reviewService = ReviewServiceImpl(memberRepository, reviewRepository,s3Service)

    @Test
    fun `존재하는 아이디로 getReview하면 ReviewResponseDto를 반환한다`() {
        val responseDto = reviewService.getReview(1L)

        responseDto.id shouldBe defaultReview.id
        responseDto.title shouldBe defaultReview.title
        responseDto.memberId shouldBe defaultReview.memberId
        responseDto.wineId shouldBe defaultReview.wineId
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
    fun `getReviews 하면 review 목록을 반환한다`() {
        val result = reviewService.getReviewList()

        result.size shouldBe defaultReviewList.size
        result.forEachIndexed { index, response ->
            response.title shouldBe defaultReviewList[index].title
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

        val testUserPrincipal = UserPrincipal(memberId = 1L, memberRole = setOf("MEMBER"))
        every { memberRepository.findById(any()) } returns Member(
            "test", "test", "test", "test"
        ).apply { id = testUserPrincipal.memberId }

        val response = reviewService.createReview(testUserPrincipal, reviewCreateRequest)

        response.title shouldBe reviewCreateRequest.title
        response.memberId shouldBe testUserPrincipal.memberId
        response.wineId shouldBe reviewCreateRequest.wineId
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
    fun `createReviewMedia를 하면 ReviewMultuMedeia가 생성된다`(){
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

        // Mock the uploadS3 method with exact values
        val reviewMultiMediaList = multiMedia.mapIndexed { index, file ->
            ReviewMultiMedia(
                reviewId = 1L,
                mediaUrl = "test$index",
                mediaType = ReviewMediaType.IMAGE
            ).apply { id = index.toLong() }
        }

        every { s3Service.upload(any(), any()) } returns ""

        val responseMedia = reviewService.createReviewMedia(testUserPrincipal, 1L, multiMedia)

        responseMedia.size shouldBe 10
        responseMedia.first().mediaUrl shouldBe "test0"
    }
}