package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewRepository
import sparta.nbcamp.wachu.exception.AccessDeniedException
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Service
class ReviewServiceImpl(
    private val memberRepository: MemberRepository,
    private val reviewRepository: ReviewRepository,
) : ReviewService {
    override fun getReviewList(): List<ReviewResponse> {
        val reviewList = reviewRepository.findAll()
        return reviewList.map { ReviewResponse.from(it) }
    }

    @Transactional(readOnly = true)
    override fun getReview(id: Long): ReviewResponse {
        val review = reviewRepository.findById(id)
            ?: throw ModelNotFoundException("Review", id)
        return ReviewResponse.from(review)
    }

    @Transactional
    override fun createReview(userPrincipal: UserPrincipal, reviewRequest: ReviewRequest): ReviewResponse {
        val member = memberRepository.findById(userPrincipal.memberId)
            ?: throw ModelNotFoundException("Member", userPrincipal.memberId)
        val review = ReviewRequest.toEntity(member.id!!, reviewRequest)
        return ReviewResponse.from(reviewRepository.save(review))
    }

    override fun deleteReview(userPrincipal: UserPrincipal, id: Long) {
        val review = reviewRepository.findById(id)
            ?: throw ModelNotFoundException("Review", id)
        check(
            review.hasPermission(
                userPrincipal.memberId,
                userPrincipal.role
            )
        ) { throw AccessDeniedException("not your review") }

        reviewRepository.delete(review)
    }
}