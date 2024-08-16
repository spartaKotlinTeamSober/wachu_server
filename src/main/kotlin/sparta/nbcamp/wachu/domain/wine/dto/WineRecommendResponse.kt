package sparta.nbcamp.wachu.domain.wine.dto

import sparta.nbcamp.wachu.infra.openai.dto.WineEmbeddingData

data class WineRecommendResponse(
    val wine: WineResponse,
    val similarityMap: Map<String, Double>,
    val totalSimilarity: Double
) {
    companion object {
        fun from(pair: Pair<WineEmbeddingData, Double>): WineRecommendResponse {
            val data = pair.first
            val totalSimilarity = pair.second
            return WineRecommendResponse(
                wine = WineResponse.convert(data.wine),
                similarityMap = data.similarityMap,
                totalSimilarity = totalSimilarity
            )
        }
    }
}