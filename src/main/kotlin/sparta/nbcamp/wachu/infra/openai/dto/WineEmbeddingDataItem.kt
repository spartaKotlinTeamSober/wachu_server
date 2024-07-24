package sparta.nbcamp.wachu.infra.openai.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class WineEmbeddingDataItem(
    val property: String,
    var embedding: List<Double>
) {
    @JsonIgnore
    fun isAroma(): Boolean {
        return property.contains(WineEmbeddingData.AROMA_PREFIX)
    }
}
