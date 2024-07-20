package sparta.nbcamp.wachu.infra.openai.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.infra.openai.client.OpenAIEmbeddingClient
import sparta.nbcamp.wachu.infra.openai.common.utils.WineEmbeddingUtils
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingCompareDto
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData

@Service
class OpenAIService(
    private val openAIEmbeddingClient: OpenAIEmbeddingClient,
    private val wineRepository: WineRepository,
) {
    fun convertJsonToEmbeddingJson(): List<WineEmbeddingData> {
        return wineRepository.findAll(PageRequest.of(0, 3))
            .content
            .map { WineEmbeddingUtils.embeddingInputFromWine(it) }
            .map { openAIEmbeddingClient.inputListToEmbeddingData(it) }
            .map { WineEmbeddingData.fromMap(it) }
    }

    fun convertInputToJson(input: String): List<Double> {
        return openAIEmbeddingClient.convertInputToOpenAiEmbedding(input)
    }

    fun compareEmbedding(target: String, compare: String): WineEmbeddingCompareDto {
        val targetEmbedding = convertInputToJson(target)
        val compareEmbedding = convertInputToJson(compare)
        val similarity = WineEmbeddingUtils.cosineSimilarity(targetEmbedding, compareEmbedding)

        return WineEmbeddingCompareDto(
            similarity = similarity,
            target = targetEmbedding,
            compare = compareEmbedding,
        )
    }
}