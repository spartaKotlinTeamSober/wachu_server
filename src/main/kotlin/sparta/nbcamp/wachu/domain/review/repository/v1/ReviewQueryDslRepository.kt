package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.review.model.v1.QReview
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class ReviewQueryDslRepository : QueryDslSupport() {
    private val review = QReview.review

    fun findPage(pageable: Pageable): Page<Review> {
        val total = queryFactory
            .select(review.count())
            .from(review)
            .fetchOne()
            ?: 0L

        val query = queryFactory
            .selectFrom(review)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val content = query.fetch()

        val page = PageImpl(content, pageable, total)
        return page
    }
}