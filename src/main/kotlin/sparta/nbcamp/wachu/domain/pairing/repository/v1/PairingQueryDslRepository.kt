package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.model.v1.QPairing
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class PairingQueryDslRepository : QueryDslSupport() {
    private val pairing = QPairing.pairing

    fun findList(page: Int, size: Int): List<Pairing> {
        val pageable = PageRequest.of(page, size)
        val query = queryFactory
            .selectFrom(pairing)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val list = query.fetch()
        return list
    }
}