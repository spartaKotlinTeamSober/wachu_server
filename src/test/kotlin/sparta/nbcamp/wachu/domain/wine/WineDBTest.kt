package sparta.nbcamp.wachu.domain.wine

import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository
import sparta.nbcamp.wachu.domain.wine.service.WineService

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
class WineDBTest {

    @Autowired
    private lateinit var wineService: WineService

    @Autowired
    private lateinit var wineJpaRepository: WineJpaRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @AfterEach
    fun deleteDB() {
        wineJpaRepository.deleteAll()
    }

    @Test
    fun `정렬이 잘 되는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wine = wineService.getWineList(
            direction = "aSc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = ""
        )
        wine[0].price shouldBe 2000

        val wine2 = wineService.getWineList(
            direction = "deSc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = ""
        )
        wine2[0].price shouldBe 77700
    }

    @Test
    fun `검색어 QUERY에 값을 넣지 않았을때 전체 리스트가 조회되는지 테스트하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wine = wineService.getWineList(
            direction = "asc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = ""
        )
        wine.size shouldBe 5

        //전체 리스트의 수는 5지만 page 의 size 를 2로 설정했으니 wine2의 size 는 2가 되어야함
        val wine2 = wineService.getWineList(
            direction = "asc",
            page = 0,
            size = 2,
            sortBy = "price",
            query = ""
        )
        wine2.size shouldBe 2
    }

    @Test
    fun `query에 값을 집어넣었을때 해당 값이 포함된 리스트가 제대로 조회되는지 테스트하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wine = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "편의점"
        )

        wine.size shouldBe 1
        wine[0].name shouldBe "편의점에서 산 와인"
        wine[0].id shouldBe 3
    }

    @Test
    fun `와인 2개 비교할때 2개의 와인 id가 제대로 들어오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.compareWine(wineIds = listOf(4, 5))

        wines[0].id shouldBe 4
        wines[1].id shouldBe 5
    }

    @Test
    fun `와인id 들어오면 제대로 찾는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wine = wineService.getWineById(wineId = 5)
        wine.name shouldBe "777"
        wine.price shouldBe 77700
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
                style = "RED",
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
                style = "WHITE",
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
                style = "RED",
                aroma = "",
                kind = "",
                price = 77700,
                country = "UK",
                region = "LONDON",
                embedding = null,
            )
        )
    }
}