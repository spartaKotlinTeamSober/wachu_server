package sparta.nbcamp.wachu.domain.review.model.v1

import jakarta.persistence.*

@Entity
@Table(name = "review_media")
class ReviewMultiMedia(
    @Column(name = "review_id")
    var reviewId: Long,

    @Column
    var mediaUrl: String,

    @Enumerated(EnumType.STRING)
    @Column
    var mediaType: ReviewMediaType
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun toEntity(reviewId: Long, mediaUrl: String, mediaType: ReviewMediaType):ReviewMultiMedia {
            return ReviewMultiMedia(
                reviewId = reviewId,
                mediaUrl = mediaUrl,
                mediaType = mediaType
            )
        }
    }
}