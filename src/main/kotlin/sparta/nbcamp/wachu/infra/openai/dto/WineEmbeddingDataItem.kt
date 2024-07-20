package sparta.nbcamp.wachu.infra.openai.dto

data class WineEmbeddingDataItem(
    val property: String,
    val embedding: List<Double>
) {
    fun isAroma(): Boolean {
        return property.contains(WineEmbeddingData.AROMA_PREFIX)
    }
}
