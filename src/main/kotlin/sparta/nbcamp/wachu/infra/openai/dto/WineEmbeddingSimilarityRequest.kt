package sparta.nbcamp.wachu.infra.openai.dto

data class WineEmbeddingSimilarityRequest(
    val input1: List<Double>,
    val input2: List<Double>
)