package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.model.v1.QPairing
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class PairingQueryDslRepository : QueryDslSupport() {
    private val pairing = QPairing.pairing

    fun findPage(pageable: Pageable): Page<Pairing> {
        val total = queryFactory
            .select(pairing.count())
            .from(pairing)
            .fetchOne()
            ?: 0L

        val query = queryFactory
            .selectFrom(pairing)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val content = query.fetch()

        val page = PageImpl(content, pageable, total)
        return page
    }
}