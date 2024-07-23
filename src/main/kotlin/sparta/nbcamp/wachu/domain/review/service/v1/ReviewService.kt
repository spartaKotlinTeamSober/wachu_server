package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewMultiMediaResponse
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

interface ReviewService {
    fun getReviewList(): List<ReviewResponse>
    fun getReview(id: Long): ReviewResponse
    fun createReview(userPrincipal: UserPrincipal, reviewRequest: ReviewRequest): ReviewResponse
    fun deleteReview(userPrincipal: UserPrincipal, id: Long)
    fun createReviewMedia(userPrincipal: UserPrincipal, reviewId: Long, multipartFileList: List<MultipartFile>):List<ReviewMultiMediaResponse>
}