package sparta.nbcamp.wachu.infra.openai.service

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository
import sparta.nbcamp.wachu.exception.ModelNotFoundException
import sparta.nbcamp.wachu.infra.batches.winedata.InMemoryCache
import sparta.nbcamp.wachu.infra.openai.common.utils.WineEmbeddingUtility
import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData

@Service
class WineEmbeddingService(
    private val wineRepository: WineRepository,
    private val embeddingUtility: WineEmbeddingUtility,
    private val inMemoryCache: InMemoryCache,
    private val wineEventListener: WineEventListener
) {
    private var minPrice: Int = 0
    private var maxPrice: Int = 0

    init {
        minPrice = wineRepository.findMinPrice()
        maxPrice = wineRepository.findMaxPrice()
    }

    fun createEveryWineEmbedding(): List<WineEmbeddingData> {
        return wineRepository.findAll()
            .map { WineEmbeddingData.fromWine(it) }
            .map { wineEmbeddingData ->
                val transformedData = embeddingUtility.inputListToEmbeddingData(
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    inputList = wineEmbeddingData.data.map { it.property }
                )

                wineEmbeddingData.copy(data = transformedData)
            }
    }

    fun createWineEmbedding(wineId: Long): WineEmbeddingData {
        // TODO: 와인 추가시 minPrice, maxPrice가 바뀔 경우 모든 Price의 벡터 데이터가 바뀌어야함
        val wine = wineRepository.findByIdOrNull(wineId) ?: throw ModelNotFoundException("wine", wineId)
        val wineEmbeddingData = WineEmbeddingData.fromWine(wine)
        val transformedData = embeddingUtility.inputListToEmbeddingData(
            minPrice = minPrice,
            maxPrice = maxPrice,
            inputList = wineEmbeddingData.data.map { it.property }
        )

        return wineEmbeddingData.copy(data = transformedData)
    }

    fun recommendWine(request: RecommendWineRequest): List<Pair<WineEmbeddingData, Double>> {
        val wineId = request.preferWineId

        if (!wineEventListener.isLoaded()) throw IllegalStateException("데이터 로드중")
        return embeddingUtility.recommendWineList(
            targetWine = inMemoryCache.getWine(wineId) ?: throw ModelNotFoundException("wine", wineId),
            everyWineList = inMemoryCache.getWines(),
            request = request
        )
    }

    fun getSimilarity(input1: List<Double>, input2: List<Double>): Double {
        return embeddingUtility.cosineSimilarity(input1, input2)
    }
}