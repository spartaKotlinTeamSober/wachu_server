package sparta.nbcamp.wachu.domain.wine.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class WineRepositoryImpl(
    private val wineJpaRepository: WineJpaRepository,
) : WineRepository, QueryDslSupport() {
    private val wine = QWine.wine

    override fun findAll(pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findAll(pageable)
    }

    override fun findByNameContaining(query: String, pageable: Pageable): Page<Wine> {
        return wineJpaRepository.findByNameContaining(query, pageable)
    }

    override fun findByIdOrNull(id: Long): Wine? {
        return wineJpaRepository.findByIdOrNull(id)
    }

    override fun searchWines(query: String, pageable: Pageable): Page<Wine> {
        val whereClause = BooleanBuilder()

        whereClause.and(
            wine.name.containsIgnoreCase(query)
                .or(wine.country.containsIgnoreCase(query)) //혹시 몰라서 이름뿐만아니라 국가 지역 까지 포함시킴, 삭제 가능 예) 이탈리아 검색하면 이탈리아산 와인도 같이 검색되는 식
                .or(wine.region.containsIgnoreCase(query))
        )

        val totalCount = queryFactory.select(wine.count())
            .from(wine)
            .where(whereClause)
            .fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(wine)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, wine))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(
        pageable: Pageable,
        path: EntityPathBase<*>
    ): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->
            OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }
}
