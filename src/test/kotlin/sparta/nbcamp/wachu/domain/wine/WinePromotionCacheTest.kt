package sparta.nbcamp.wachu.domain.wine

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.PagedModel
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.PromotionStatus
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
 class WinePromotionCacheTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test serialization and deserialization of PagedModel`() {
        val promotionWineResponse = PromotionWineResponse(
            closedAt = LocalDateTime.now(),
            openedAt = LocalDateTime.now(),
            promotionId = 1,
            promotionStatus = PromotionStatus.PROMOTION,
            wine = WineResponse(
                id = 8000,
                name = "forTest",
                acidity = 1,
                body = 1,
                aroma = "1",
                tannin = 1,
                country = null,
                region = null,
                kind = null,
                price = null,
                style = null,
                sweetness = 1,
                wineType = WineType.WHITE,
            )
        )

        val pagedModel = PagedModel.of(listOf(promotionWineResponse), PagedModel.PageMetadata(1, 0, 1))

        // 직렬화 테스트
        val json = objectMapper.writeValueAsString(pagedModel)
        println("Serialized JSON: $json")

        // 역직렬화 테스트
        val deserializedObject = objectMapper.readValue(json, object : TypeReference<PagedModel<PromotionWineResponse>>() {})
        println("Deserialized Object: $deserializedObject")

        // 객체 비교 (기본적으로 PagedModel 내부 요소를 비교)
        assertEquals(pagedModel.content, deserializedObject.content)
    }
}