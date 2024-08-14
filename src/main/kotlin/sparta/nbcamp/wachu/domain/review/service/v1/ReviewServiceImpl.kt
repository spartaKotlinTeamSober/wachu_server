package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewMultiMediaResponse
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMediaType
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewMultiMediaRepository
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewRepository
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Service
class ReviewServiceImpl(
    private val wineRepository: WineRepository,
    private val memberRepository: MemberRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewMediaRepository: ReviewMultiMediaRepository,
    private val mediaS3Service: MediaS3Service
) : ReviewService {

    @Transactional(readOnly = true)
    override fun getReviewPage(pageable: Pageable): Page<ReviewResponse> {
        val reviews = reviewRepository.findAll(pageable)
        val memberIds = reviews.map { it.memberId }.toList()
        val members = memberRepository.findAllById(memberIds).associateBy { it.id }
        return reviews.map { review -> ReviewResponse.from(review, members[review.memberId]!!) }
    }

    @Transactional(readOnly = true)
    override fun getReview(id: Long): ReviewResponse {
        val review = reviewRepository.findById(id)
            ?: throw ModelNotFoundException("Review", id)
        val member = memberRepository.findById(review.memberId)
            ?: throw ModelNotFoundException("Member", review.memberId)
        val mediaList = reviewMediaRepository.mediaFindAll(id).map { ReviewMultiMediaResponse.from(it) }
        return ReviewResponse.from(review, member, mediaList)
    }

    @Transactional
    override fun createReview(
        userPrincipal: UserPrincipal,
        reviewRequest: ReviewRequest,
        images: List<MultipartFile>?
    ): ReviewResponse {
        val wine = wineRepository.findByIdOrNull(reviewRequest.wineId)
            ?: throw ModelNotFoundException("Wine", reviewRequest.wineId)
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        val review = reviewRepository.save(reviewRequest.toEntity(wine, member.id!!))

        var mediaList: List<ReviewMultiMediaResponse> = emptyList()

        if (!images.isNullOrEmpty()) {
            mediaList = mediaS3Service.upload(images, S3FilePath.REVIEW.path + "${review.id}/")
                .let { it.map { url -> ReviewMultiMedia.toEntity(review.id!!, url, ReviewMediaType.IMAGE) } }
                .let { reviewMediaRepository.mediaSave(it) }
                .let { it.map { multiMedia -> ReviewMultiMediaResponse.from(multiMedia) } }
        }

        return ReviewResponse.from(review, member, mediaList)
    }

    @Transactional
    override fun deleteReview(userPrincipal: UserPrincipal, id: Long) {
        val review = reviewRepository.findById(id)
            ?: throw ModelNotFoundException("Review", id)
        check(
            review.hasPermission(
                userPrincipal.memberId,
                userPrincipal.role
            )
        ) { throw AccessDeniedException("not your review") }

        reviewMediaRepository.deleteAllByReviewId(id)
        reviewRepository.delete(review)
    }

    @Transactional
    override fun createReviewMedia(
        userPrincipal: UserPrincipal,
        reviewId: Long,
        multipartFileList: List<MultipartFile>
    ): List<ReviewMultiMediaResponse> {
        val review = reviewRepository.findById(reviewId)
            ?: throw ModelNotFoundException("Review", reviewId)
        check(
            review.hasPermission(
                userPrincipal.memberId,
                userPrincipal.role
            )
        ) { throw AccessDeniedException("not your review") }

        val mediaList = mediaS3Service.upload(multipartFileList, S3FilePath.REVIEW.path + "$reviewId/")
            .let { it.map { url -> ReviewMultiMedia.toEntity(reviewId, url, ReviewMediaType.IMAGE) } }
        return reviewMediaRepository.mediaSave(mediaList).map { ReviewMultiMediaResponse.from(it) }
    }

    override fun getReviewMultiMedia(reviewId: Long): List<ReviewMultiMediaResponse> {
        return reviewMediaRepository.mediaFindAll(reviewId).map { ReviewMultiMediaResponse.from(it) }
    }
}