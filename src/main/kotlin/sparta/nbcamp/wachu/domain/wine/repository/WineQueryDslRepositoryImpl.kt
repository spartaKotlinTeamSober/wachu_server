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
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.wine.entity.QWine
import sparta.nbcamp.wachu.domain.wine.entity.QWinePromotion
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.infra.querydsl.QueryDslSupport

@Repository
class WineQueryDslRepositoryImpl : WineQueryDslRepository, QueryDslSupport() {

    private val wine = QWine.wine
    override fun searchWines(
        query: String,
        price: Int?,
        acidity: List<Int>?,
        body: List<Int>?,
        sweetness: List<Int>?,
        tannin: List<Int>?,
        type: String?,
        pageable: Pageable,
    ): Page<Wine> {
        val whereClause = BooleanBuilder()

        whereClause.and(
            wine.name.containsIgnoreCase(query)
                .or(wine.country.containsIgnoreCase(query))
                .or(wine.region.containsIgnoreCase(query))
        )
        if (price != null) whereClause.and(wine.price.loe(price))// 입력받은 가격보다 낮게 조회 예) 54000원 입력시 54000원 이하의 와인 조회

        if (acidity != null) whereClause.and(wine.acidity.between(acidity[0], acidity[1]))

        if (body != null) whereClause.and(wine.body.between(body[0], body[1]))

        if (sweetness != null) whereClause.and(wine.sweetness.between(sweetness[0], sweetness[1]))

        if (tannin != null) whereClause.and(wine.tannin.between(tannin[0], tannin[1]))

        if (!type.isNullOrBlank()) {
            val wineTypeEnum = WineType.values().find { it.name.equals(type, ignoreCase = true) }
            if (wineTypeEnum != null) {
                whereClause.and(wine.wineType.eq(wineTypeEnum))
            }
        }

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

    override fun findPromotionWineList(pageable: Pageable): Page<WinePromotion> {
        val startTime = System.currentTimeMillis()
        val winePromotion = QWinePromotion.winePromotion
        val wine = QWine.wine

        val baseQuery = queryFactory.selectFrom(winePromotion)
            .leftJoin(winePromotion.wine, wine).fetchJoin()

        val results = baseQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, winePromotion))
            .fetch()

        val countQuery = queryFactory.select(winePromotion.count()).from(winePromotion).fetchOne()

        val content = results //TODO() DTO대신 ENTITY에서 가져오기

        val endTime = System.currentTimeMillis()

        println("걸린 시간 = ${endTime - startTime}. ")
        return PageImpl(content, pageable, countQuery!!)
    }
}