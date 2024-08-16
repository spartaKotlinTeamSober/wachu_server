package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.common.QueryDslUtils.getOrderSpecifier
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.model.v1.QPairing
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class PairingQueryDslRepository : QueryDslSupport() {
    private val pairing = QPairing.pairing
    private val wine = QWine.wine

    fun findPage(pageable: Pageable): Page<Pairing> {
        val content = queryFactory
            .selectFrom(pairing)
            .leftJoin(pairing.wine, wine).fetchJoin()
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, pairing))
            .fetch()

        val total = queryFactory
            .select(pairing.count())
            .from(pairing)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}