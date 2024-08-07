package sparta.nbcamp.wachu.domain.wine

import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import sparta.nbcamp.wachu.WachuApplication
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.service.WineService

@Transactional
@SpringBootTest(classes = [WachuApplication::class])
//@ActiveProfiles("test")
class WineDBTest {

    @Autowired
    private lateinit var wineService: WineService

    @Test
    fun `정렬이 잘 되는지 테스트 하는 함수`() {

        val wine: Page<WineResponse> = wineService.getWineList(
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

        val wine2: Page<WineResponse> = wineService.getWineList(
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

        if (wine.content.size > 1) {
            for (i in 0 until wine.content.size - 1) {
                wine.content[i].price!!.shouldBeLessThanOrEqual(wine.content[i + 1].price!!)
            }
        }

        // Check if the list is sorted in descending order
        if (wine2.content.size > 1) {
            for (i in 0 until wine2.content.size - 1) {
                wine2.content[i].price!!.shouldBeGreaterThanOrEqual(wine2.content[i + 1].price!!)
            }
        }
    }

    @Test
    fun `query에 값을 집어넣었을때 해당 값이 포함된 리스트가 제대로 조회되는지 테스트하는 함수`() {

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

        wine.content.size shouldBe 0
    }

    @Test
    fun `와인 종류를 넣으면 해당 종류에 해당하는 와인이 나오는지 테스트 하는 함수`() {

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

        wines.content.all { it.wineType == WineType.RED } shouldBe true
    }

    @Test
    fun `와인 2개 비교할때 2개의 와인 id가 제대로 들어오는지 테스트 하는 함수`() {

        val wines = wineService.compareWine(wineIds = listOf(4, 5))

        wines[0].id shouldBe 4
        wines[1].id shouldBe 5
    }

    @Test
    fun `당도나 price에  값을 넣고 query를 검색했을때  해당 값에 해당하는 와인이 나오는지 테스트 하는 함수`() {

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
        wines.content.all { it.acidity in 1..1000 && it.sweetness in 1..1000 } shouldBe true
    }

    @Test
    fun `검색어와 조건을 같이 넣었을때 제대로 올바른 리스트를 반환하는지 테스트 하는 함수`() {

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

        println("Filtered Wines: ${wines.map { it.name }}")
        wines.content.all { it.name.contains("와인") && it.acidity in 1..130 && it.sweetness in 1..1000 } shouldBe true
    }

    @Test
    fun `금액을 입력하면 해당 금액 이하의 와인 리스트가 나오는지 테스트 하는 함수`() {

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

        wines.content.all { it.price!! <= 10000 } shouldBe true
    }
}