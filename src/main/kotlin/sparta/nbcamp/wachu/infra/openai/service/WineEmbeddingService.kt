package sparta.nbcamp.wachu.infra.openai.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.infra.openai.client.OpenAIEmbeddingClient
import sparta.nbcamp.wachu.infra.openai.common.utils.WineEmbeddingUtility
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingCompareResponse
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData

@Service
class WineEmbeddingService(
    private val openAIEmbeddingClient: OpenAIEmbeddingClient,
    private val wineRepository: WineRepository,
    private val embeddingUtility: WineEmbeddingUtility,
) {
    private var minPrice: Int = 0
    private var maxPrice: Int = 0

    init {
        minPrice = wineRepository.findMinPrice()
        maxPrice = wineRepository.findMaxPrice()
    }

    fun convertJsonToEmbeddingJson(): List<WineEmbeddingData> {
        return wineRepository.findAll(Pageable.unpaged()).content
            .map { embeddingUtility.embeddingInputFromWine(it) }
            .map {
                embeddingUtility.inputListToEmbeddingData(
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    inputList = it
                )
            }
            .map { WineEmbeddingData.fromMap(it) }
    }

    fun convertInputToJson(input: String): List<Double> {
        return openAIEmbeddingClient.convertInputToOpenAiEmbedding(input)
    }

    fun compareEmbedding(target: String, compare: String): WineEmbeddingCompareResponse {
        val targetEmbedding = convertInputToJson(target)
        val compareEmbedding = convertInputToJson(compare)
        val similarity = embeddingUtility.cosineSimilarity(targetEmbedding, compareEmbedding)

        return WineEmbeddingCompareResponse(
            similarity = similarity,
            target = targetEmbedding,
            compare = compareEmbedding,
        )
    }

    fun retrieveEmbedding(property: String): WineEmbeddingData {
        return WineEmbeddingData.fromMap(
            embeddingUtility.inputListToEmbeddingData(
                minPrice = minPrice,
                maxPrice = maxPrice,
                inputList = listOf(property)
            )
        )
    }
}