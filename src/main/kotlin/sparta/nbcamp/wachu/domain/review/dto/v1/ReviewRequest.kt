package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.wine.entity.Wine

data class ReviewRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val score: Double,
) {
    init {
        validate()
    }

    private fun validate() {
        require(isScoreValid()) { "invalid score" }
    }

    private fun isScoreValid(): Boolean {
        return this.score in 0.0..5.0 && this.score % 0.5 == 0.0
    }

    fun toEntity(wine: Wine, memberId: Long): Review {
        return Review(
            wine = wine,
            memberId = memberId,
            title = this.title,
            description = this.description,
            score = this.score,
        )
    }
}
