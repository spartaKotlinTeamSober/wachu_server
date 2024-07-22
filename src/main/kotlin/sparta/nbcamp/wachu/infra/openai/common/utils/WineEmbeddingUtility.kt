package sparta.nbcamp.wachu.infra.openai.common.utils

import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.infra.openai.client.OpenAIEmbeddingClient
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData
import kotlin.math.sqrt

@Component
class WineEmbeddingUtility(
    private val jsonFileHandler: WineEmbeddingJsonHandler,
    private val openAIEmbeddingClient: OpenAIEmbeddingClient
) {
    private val embeddingCache = jsonFileHandler.readData().toMutableMap()

    fun cosineSimilarity(vec1: List<Double>, vec2: List<Double>): Double {
        val dotProduct = vec1.zip(vec2) { a, b -> a * b }.sum()
        val magnitude1 = sqrt(vec1.sumOf { it * it })
        val magnitude2 = sqrt(vec2.sumOf { it * it })
        return dotProduct / (magnitude1 * magnitude2)
    }

    fun recommendWineList(
        targetEmbedding: List<Double>,
        embeddings: List<List<Double>>,
        top: Int = 5
    ): List<Pair<Int, Double>> {
        return embeddings.mapIndexed { index, embedding ->
            index + 1 to cosineSimilarity(targetEmbedding, embedding)
        }
            .sortedByDescending { it.second }
            .take(top)
    }

    fun embeddingInputFromWine(wine: Wine): List<String> {
        return mutableListOf(
            "sweetness:${wine.sweetness}",
            "acidity:${wine.acidity}",
            "body:${wine.body}",
            "tannin:${wine.tannin}",
            "type:${wine.wineType}",
            "price:${wine.price}"
        ).apply {
            addAll(parseAromaToList(wine.aroma))
        }
    }

    private fun parseAromaToList(aroma: String): List<String> {
        val aromaList = mutableListOf<String>()

        // (\w+) 단어 문자를 포함하는 첫 번째 캡처 그룹
        // = 는 key와 value를 구분하는 구분자로 그냥 텍스트
        // \[ 대괄호의 시작점 찾기
        // ([^]]+) 대괄호 ]을 제외한 문자를 포함하는 두 번째 캡처 그룹, 대괄호 빼고 매칭됨
        // \] 대괄호의 종료점 찾기
        val aromaPairs = Regex("""(\w+)=\[([^]]+)]""").findAll(aroma)

        aromaPairs.forEach { matchResult ->
            val key = matchResult.groupValues[1]
            val values = matchResult.groupValues[2]
            values.split(", ").forEach { value ->
                aromaList.add("${WineEmbeddingData.AROMA_PREFIX}${key.trim()}:${value.trim()}")
            }
        }

        return aromaList
    }

    private fun priceMinMaxScaling(price: Int, minPrice: Int = 1, maxPrice: Int = 10000000): Double {
        return (price - minPrice).toDouble() / (maxPrice - minPrice)
    }

    fun retrieveEmbeddingsIfAbsent(minPrice: Int, maxPrice: Int, property: String): List<Double> {
        return embeddingCache[property]
            ?: run {
                val data =
                    if (property.contains("price")) {
                        listOf(priceMinMaxScaling(property.split(":")[1].toInt(), minPrice, maxPrice))
                    } else {
                        openAIEmbeddingClient.convertInputToOpenAiEmbedding(property)
                    }

                embeddingCache[property] = data
                jsonFileHandler.writeData(embeddingCache)
                data
            }
    }

    fun inputListToEmbeddingData(minPrice: Int, maxPrice: Int, inputList: List<String>): Map<String, List<Double>> {
        val dataMap = mutableMapOf<String, List<Double>>()

        inputList.forEach { input ->
            val property = input.trim()
            val embedding = retrieveEmbeddingsIfAbsent(minPrice, maxPrice, property)

            dataMap[property] = embedding
        }

        return dataMap
    }
}