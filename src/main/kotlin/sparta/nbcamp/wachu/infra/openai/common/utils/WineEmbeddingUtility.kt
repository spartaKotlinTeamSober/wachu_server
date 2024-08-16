package sparta.nbcamp.wachu.infra.openai.common.utils

import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import sparta.nbcamp.wachu.infra.openai.client.OpenAIEmbeddingClient
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData.Companion.AROMA_KEY
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData.Companion.AROMA_PREFIX
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData.Companion.PRICE_KEY
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData.Companion.TASTY_KEY
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingDataItem
import kotlin.math.abs
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
        targetWine: Wine,
        everyWineList: List<Wine>,
        request: RecommendWineRequest,
        top: Int = 10
    ): List<Pair<WineEmbeddingData, Double>> {
        val targetEmbeddingData = WineEmbeddingData.fromWine(targetWine)
            .apply { data.forEach { it.embedding = retrieveEmbedding(it.property) } }

        return everyWineList
            .filter { it.id != targetWine.id }
            .map { compareWine ->
                val compareEmbeddingData = WineEmbeddingData.fromWine(compareWine)
                    .apply { data.forEach { it.embedding = retrieveEmbedding(it.property) } }

                val similarityMap = compareWineEmbeddingData(
                    targetData = targetEmbeddingData,
                    compareData = compareEmbeddingData,
                    request = request
                )

                compareEmbeddingData.similarityMap = similarityMap

                compareEmbeddingData to similarityMap.values.average()
            }
            .sortedByDescending { it.second }
            .take(top)
    }

    fun compareEmbedding(target: List<Double>, compare: List<Double>): Double {
        return cosineSimilarity(target, compare)
    }

    fun compareWineEmbeddingData(
        targetData: WineEmbeddingData,
        compareData: WineEmbeddingData,
        request: RecommendWineRequest
    ): Map<String, Double> {
        val similarityMap = mutableMapOf<String, Double>()

        targetData.data.forEach { targetItem ->
            val key = targetItem.property.split(":")[0]
            val compareItem = compareData.data.find { it.property.contains(key) }
            if (compareItem != null) {
                if (key == PRICE_KEY) {
                    similarityMap[PRICE_KEY] =
                        (1 - abs(targetItem.embedding[0] - compareItem.embedding[0])) * (1 + (request.priceWeight / 100.0))
                } else if (!key.contains(AROMA_PREFIX)) {
                    similarityMap[TASTY_KEY] =
                        cosineSimilarity(
                            targetItem.embedding, compareItem.embedding
                        ) * (1 + (request.tastyWeight / 100.0))
                }
            }
        }

        val targetAromaList = targetData.aromaFilteredList().data
        val compareAromaList = compareData.aromaFilteredList().data

        val targetAromaEmbeddingSum = sumVector(
            targetAromaList.map { it.embedding }
        ).map { it / targetAromaList.size }

        val compareAromaEmbeddingSum = sumVector(
            compareAromaList.map { it.embedding }
        ).map { it / compareAromaList.size }

        similarityMap[AROMA_KEY] =
            compareEmbedding(
                targetAromaEmbeddingSum,
                compareAromaEmbeddingSum
            ) * (1 + (request.aromaWeight / 100.0))

        return similarityMap
    }

    fun retrieveEmbedding(property: String): List<Double> {
        return embeddingCache[property] ?: listOf()
    }

    fun retrieveEmbeddingsIfAbsent(minPrice: Int, maxPrice: Int, property: String): List<Double> {
        return embeddingCache[property]
            ?: run {
                val data =
                    if (property.contains("price")) {
                        val price = property.split(":")[1].toInt()
                        listOf(priceMinMaxScaling(price, minPrice, maxPrice))
                    } else {
                        openAIEmbeddingClient.convertInputToOpenAiEmbedding(property)
                    }

                embeddingCache[property] = data
                jsonFileHandler.writeData(embeddingCache)
                data
            }
    }

    fun inputListToEmbeddingData(minPrice: Int, maxPrice: Int, inputList: List<String>): List<WineEmbeddingDataItem> {
        val dataMap = mutableMapOf<String, List<Double>>()

        inputList.forEach { input ->
            val property = input.trim()
            val embedding = retrieveEmbeddingsIfAbsent(minPrice, maxPrice, property)

            dataMap[property] = embedding
        }

        return WineEmbeddingData.fromMap(dataMap)
    }

    private fun sumVector(vectorList: List<List<Double>>): List<Double> {
        if (vectorList.isEmpty()) return emptyList()

        val numElements = vectorList[0].size
        val sums = MutableList(numElements) { 0.0 }

        vectorList.forEach { vector ->
            for (i in vector.indices) {
                sums[i] += vector[i]
            }
        }

        return sums
    }

    private fun priceMinMaxScaling(price: Int, minPrice: Int = 1, maxPrice: Int = 10000000): Double {
        return (price - minPrice).toDouble() / (maxPrice - minPrice)
    }
}