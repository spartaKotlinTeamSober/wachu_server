package sparta.nbcamp.wachu.domain.wine

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.domain.wine.repository.WineJpaRepository
import sparta.nbcamp.wachu.domain.wine.service.WineService

@SpringBootTest
@ActiveProfiles("test")
class WineServiceTest {

    @Autowired
    private lateinit var wineService: WineService

    @Autowired
    private lateinit var wineJpaRepository: WineJpaRepository

    @Test
    fun `검색어 QUERY에 의해 조회된 결과가 0건일 경우 테스트하는 함수`() {

        wineJpaRepository.saveAllAndFlush(DEFAULT_WINE_LIST)
        wineService.getWineList(
            direction = "asc",
            page = 1,
            size = 2,
            sortBy = "price",
            query = "휘바휘바1!"
        )
    }

    companion object {

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
            )
        )
    }
}