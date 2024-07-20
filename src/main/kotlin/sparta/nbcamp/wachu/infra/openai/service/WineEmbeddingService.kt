package sparta.nbcamp.wachu.infra.openai.service

import org.springframework.data.domain.PageRequest
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
    fun convertJsonToEmbeddingJson(): List<WineEmbeddingData> {
        return wineRepository.findAll(PageRequest.of(0, 3)).content
            .map { embeddingUtility.embeddingInputFromWine(it) }
            .map { embeddingUtility.inputListToEmbeddingData(openAIEmbeddingClient, it) }
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
}