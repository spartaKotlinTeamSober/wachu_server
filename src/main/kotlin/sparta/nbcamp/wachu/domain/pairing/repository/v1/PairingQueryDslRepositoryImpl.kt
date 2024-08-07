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
class PairingQueryDslRepositoryImpl : QueryDslSupport(), PairingQueryDslRepository {
    private val pairing = QPairing.pairing
    private val wine = QWine.wine

    override fun findFetchJoin(wineId: Long, pageable: Pageable): Page<Pairing> {
        val content = queryFactory
            .selectFrom(pairing)
            .leftJoin(pairing.wine, wine).fetchJoin()
            .where(pairing.wine.id.eq(wineId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = content.size.toLong()

        return PageImpl(content, pageable, total)
    }
}