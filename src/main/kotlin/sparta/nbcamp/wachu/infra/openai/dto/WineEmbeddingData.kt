package sparta.nbcamp.wachu.infra.openai.dto

data class WineEmbeddingData(
    val data: List<WineEmbeddingDataItem>
) {
    fun aromaFilteredList(): List<WineEmbeddingDataItem> {
        return this.data.filter { it.isAroma() }
    }

    companion object {
        const val AROMA_PREFIX = "aroma_"

        fun fromMap(data: Map<String, List<Double>>): WineEmbeddingData {
            return WineEmbeddingData(
                data.map { WineEmbeddingDataItem(it.key, it.value) }
            )
        }
    }
}

