package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.model.v1.QPairing
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class PairingQueryDslRepository : QueryDslSupport() {
    private val pairing = QPairing.pairing

    fun findPage(pageable: Pageable): List<Pairing> {
        val query = queryFactory
            .selectFrom(pairing)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val page = query.fetch()
        return page
    }
}