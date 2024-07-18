package sparta.nbcamp.wachu.domain.wine

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.wine.entity.PromotionStatus
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.service.WineService
import java.time.LocalDateTime

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class WinePromotionDBTest {

    @Autowired
    private lateinit var wineService: WineService

    @SpyBean
    private lateinit var entityManager: EntityManager

    @Test
    fun `fetch join 전 후 시간이 빨라졌는지 확인하는  테스트`() {
    }

    companion object {

        // 현재 테스트에서는 DEFAULT_MEMBER_LIST 를 사용하지 않지만 남겨놓음
        private val DEFAULT_MEMBER_LIST = listOf(
            Member(email = "test1@naver.com", password = "test1", nickname = "test1", profileImageUrl = null)
        )
        private val DEFAULT_WINE_LIST = listOf(
            Wine(
                name = "테스트용 와인 이름1",
                acidity = 1,
                body = 3,
                sweetness = 10,
                tannin = 5,
                wineType = WineType.WHITE,
                style = "",
                aroma = "",
                kind = "",
                price = 10000,
                country = "Italy",
                region = "roma",
                embedding = null,
            ),
            Wine(
                name = "테스트용 와인 이름2",
                acidity = 100,
                body = 300,
                sweetness = 1000,
                tannin = 500,
                wineType = WineType.RED,
                style = "",
                aroma = "",
                kind = "",
                price = 10000,
                country = "KOREA",
                region = "SEOUL",
                embedding = null,
            ),
            Wine(
                name = "편의점에서 산 와인",
                acidity = 110,
                body = 1300,
                sweetness = 11000,
                tannin = 1500,
                wineType = WineType.ROSE,
                style = "RED",
                aroma = "",
                kind = "",
                price = 2000,
                country = "KOREA",
                region = "SEOUL",
                embedding = null,
            ),
            Wine(
                name = "이마트에서 산 와인",
                acidity = 1100,
                body = 132,
                sweetness = 21000,
                tannin = 1200,
                wineType = WineType.SPARKLING,
                style = "RED",
                aroma = "",
                kind = "",
                price = 2000,
                country = "KOREA",
                region = "SEOUL",
                embedding = null,
            ),
            Wine(
                name = "777",
                acidity = 777,
                body = 777,
                sweetness = 777,
                tannin = 777,
                wineType = WineType.RED,
                style = "",
                aroma = "",
                kind = "",
                price = 77700,
                country = "UK",
                region = "LONDON",
                embedding = null,
            ),
            Wine(
                name = "부산 사나이 와인",
                acidity = 10000,
                body = 0,
                sweetness = 0,
                tannin = 10000,
                wineType = WineType.FORTIFIED,
                style = "",
                aroma = "",
                kind = "",
                price = 2000,
                country = "KOREA",
                region = "BUSAN",
                embedding = null,
            ),
            Wine(
                name = "농심 와인",
                acidity = 3,
                body = 3,
                sweetness = 2,
                tannin = 7,
                wineType = WineType.WHITE,
                style = "",
                aroma = "",
                kind = "",
                price = 77700,
                country = "KOREA",
                region = "SEOUL",
                embedding = null,
            )
        )

        private val DEFAULT_Promotion_WINE_LIST = listOf(
            WinePromotion(
                wine = Wine(
                    name = "농심 와인",
                    acidity = 3,
                    body = 3,
                    sweetness = 2,
                    tannin = 7,
                    wineType = WineType.WHITE,
                    style = "",
                    aroma = "",
                    kind = "",
                    price = 77700,
                    country = "KOREA",
                    region = "SEOUL",
                    embedding = null,
                ),
                closedAt = LocalDateTime.now(),
                openedAt = LocalDateTime.now(),
                status = PromotionStatus.PROMOTION,
                id = 1
            ),
            WinePromotion(
                wine = Wine(
                    name = "편의점 와인",
                    acidity = 30,
                    body = 30,
                    sweetness = 20,
                    tannin = 70,
                    wineType = WineType.WHITE,
                    style = "",
                    aroma = "",
                    kind = "",
                    price = 77700,
                    country = "KOREA",
                    region = "SEOUL",
                    embedding = null,
                ),
                closedAt = LocalDateTime.now(),
                openedAt = LocalDateTime.now(),
                status = PromotionStatus.PROMOTION,
                id = 2
            )
        )
    }
}