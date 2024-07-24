package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.review.model.v1.QReview
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class ReviewQueryDslRepository : QueryDslSupport() {
    private val review = QReview.review
    private val wine = QWine.wine

    fun findPage(pageable: Pageable): Page<Review> {
        val content = queryFactory
            .selectFrom(review)
            .leftJoin(review.wine, wine).fetchJoin()
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(review.count())
            .from(review)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}