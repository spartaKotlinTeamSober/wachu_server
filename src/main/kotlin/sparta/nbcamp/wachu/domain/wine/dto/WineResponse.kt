package sparta.nbcamp.wachu.domain.wine.dto

data class WineResponse(

    val id: Long,
    val sweetness: Int,
    val acidity: Int,
    val body: Int,
    val tannin: Int,
    val type: String,
    val aroma: String,
    val price: Int,
    val kind: String,
    val style: String,
    val country: String,
    val region: String,
)
