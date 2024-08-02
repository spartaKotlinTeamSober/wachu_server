package sparta.nbcamp.wachu.domain.wine.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.domain.wine.entity.QWinePromotion
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class WinePromotionRepositoryImpl : WinePromotionRepository, QueryDslSupport() {

    private val wine = QWine.wine
    private val winePromotion = QWinePromotion.winePromotion

    override fun findPromotionWineList(pageable: Pageable): Page<PromotionWineResponse> {
        val winePromotion = QWinePromotion.winePromotion
        val wine = QWine.wine

        val results = queryFactory.selectFrom(winePromotion)
            .leftJoin(winePromotion.wine, wine).fetchJoin()
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, winePromotion))
            .fetch()
            .map { PromotionWineResponse.from(it) }

        val countQuery = queryFactory.select(winePromotion.count()).from(winePromotion).fetchOne()

        return PageImpl(results, pageable, countQuery!!)
    }

    override fun findAllWithoutFetchJoinForTest(pageable: Pageable): Page<PromotionWineResponse> {

        val baseQuery = queryFactory.selectFrom(winePromotion)
            .leftJoin(winePromotion.wine, wine)

        val results = baseQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, winePromotion))
            .fetch()
            .map { PromotionWineResponse.from(it) }

        val countQuery = queryFactory.select(winePromotion.count()).from(winePromotion).fetchOne()

        return PageImpl(results, pageable, countQuery!!)
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