package sparta.nbcamp.wachu.domain.wine

import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository
import sparta.nbcamp.wachu.domain.wine.service.WineService

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
@ActiveProfiles("test")
class WineDBTest {

    @Autowired
    private lateinit var wineService: WineService

    @Autowired
    private lateinit var wineJpaRepository: WineJpaRepository

    @Test
    fun `정렬이 잘 되는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wine = wineService.getWineList(
            direction = "aSc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = null,
        )
        wine[0].price shouldBe 2000

        val wine2 = wineService.getWineList(
            direction = "deSc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = null,
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
            query = "",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = null,
        )
        wine.size shouldBe 7

        val wine2 = wineService.getWineList(
            direction = "asc",
            page = 0,
            size = 2,
            sortBy = "price",
            query = "",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = null,
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
            query = "편의점",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = null,
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.name.contains("편의점") }
                .sortedByDescending { it.price }

        wine.size shouldBe `직접 필터링 한 값`.size
    }

    @Test
    fun `와인 종류를 넣으면 해당 종류에 해당하는 와인이 나오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = null,
            price = null,
            tannin = null,
            sweetness = null,
            type = "RED",
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.wineType == WineType.RED }
                .sortedByDescending { it.price }


        wines.size shouldBe `직접 필터링 한 값`.size
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

    @Test
    fun `당도에 값을 넣으면 해당 값에 해당하는 와인이 나오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = listOf(0, 999),
            price = null,
            tannin = null,
            sweetness = listOf(300, 777),
            type = null,
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.body in 0..999 && it.sweetness in 300..777 }
                .sortedByDescending { it.price }
        wines[0].price shouldBe `직접 필터링 한 값`[0].price
        wines[0].name shouldBe `직접 필터링 한 값`[0].name
    }

    @Test
    fun `한가지의 조건이 틀렸을때 아무것도 안나오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = listOf(700, 999),
            price = null,
            tannin = null,
            sweetness = listOf(300, 777),
            type = "UNDEFINED",
        )
        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.wineType == WineType.UNDEFINED && it.body in 700..999 && it.sweetness in 300..777 }
                .sortedByDescending { it.price }

        wines.size shouldBe `직접 필터링 한 값`.count()
    }

    @Test
    fun `당도나 price에  값을 넣고 query를 검색했을때  해당 값에 해당하는 와인이 나오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "",
            acidity = null,
            body = listOf(1, 1000),
            price = null,
            tannin = null,
            sweetness = listOf(1, 1000),
            type = null,
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.body in 1..1000 && it.sweetness in 1..1000 }.sortedByDescending { it.price }


        wines[0].price shouldBe `직접 필터링 한 값`[0].price
        wines[0].name shouldBe `직접 필터링 한 값`[0].name
    }

    @Test
    fun `검색어와 조건을 같이 넣었을때 제대로 올바른 리스트를 반환하는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "와인",
            acidity = listOf(1, 130),
            body = null,
            price = null,
            tannin = null,
            sweetness = listOf(1, 1000),
            type = null,
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.name.contains("와인") && it.acidity in 1..130 && it.sweetness in 1..1000 }
                .sortedByDescending { it.price }.count()
        println("Filtered Wines: ${wines.map { it.name }}")
        wines.size shouldBe `직접 필터링 한 값`
    }

    @Test
    fun `금액을 입력하면 해당 금액 이하의 와인 리스트가 나오는지 테스트 하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)

        val wines = wineService.getWineList(
            direction = "desc",
            page = 0,
            size = 10,
            sortBy = "price",
            query = "와인",
            acidity = listOf(1, 130),
            body = null,
            price = 10000,
            tannin = null,
            sweetness = listOf(1, 1000),
            type = null,
        )

        val `직접 필터링 한 값` =
            DEFAULT_WINE_LIST.filter { it.name.contains("와인") && it.acidity in 1..130 && it.sweetness in 1..1000 && it.price in 0..10000 }
                .sortedByDescending { it.price }

        wines.size shouldBe 2
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
    }
}