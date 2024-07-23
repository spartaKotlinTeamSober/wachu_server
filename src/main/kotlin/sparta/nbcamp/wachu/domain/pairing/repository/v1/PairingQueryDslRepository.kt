package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.model.v1.QPairing
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class PairingQueryDslRepository : QueryDslSupport() {
    private val pairing = QPairing.pairing
    private val wine = QWine.wine

    fun findPage(pageable: Pageable): Page<Pairing> {
        val basicResults = queryFactory
            .selectFrom(pairing)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(pairing.count())
            .from(pairing)
            .fetchOne() ?: 0L

        val content = basicResults.mapNotNull { basicPairing ->
            queryFactory
                .selectFrom(pairing)
                .leftJoin(pairing.wine, wine).fetchJoin()
                .where(pairing.id.eq(basicPairing.id))
                .fetchOne()
        }

        return PageImpl(content, pageable, total)
    }
}