package sparta.nbcamp.wachu.infra.openai.dto

import sparta.nbcamp.wachu.domain.wine.entity.Wine

data class WineEmbeddingData(
    val wine: WineDataResponse,
    val data: List<WineEmbeddingDataItem>,
    var similarityMap: Map<String, Double> = emptyMap()
) {
    fun aromaFilteredList(): WineEmbeddingData {
        return WineEmbeddingData(wine, this.data.filter { it.isAroma() })
    }

    companion object {
        const val AROMA_PREFIX = "aroma_"
        const val AROMA_KEY = "aroma"
        const val PRICE_KEY = "price"
        const val TASTY_KEY = "tasty"

        fun fromMap(data: Map<String, List<Double>>): List<WineEmbeddingDataItem> {
            return data.map { WineEmbeddingDataItem(it.key, it.value) }
        }

        fun fromWine(wine: Wine): WineEmbeddingData {
            return embeddingPropertiesFromWine(wine)
                .let { inputList ->
                    val map = mutableMapOf<String, List<Double>>()
                    inputList.forEach { input ->
                        map[input] = emptyList()
                    }
                    fromMap(map)
                }
                .let {
                    WineEmbeddingData(
                        WineDataResponse.from(wine),
                        it
                    )
                }
        }

        fun embeddingPropertiesFromWine(wine: Wine): List<String> {
            return mutableListOf(
                "sweetness:${wine.sweetness}",
                "acidity:${wine.acidity}",
                "body:${wine.body}",
                "tannin:${wine.tannin}",
                "type:${wine.wineType}",
                "price:${wine.price}"
            ).apply {
                addAll(parseAromaToList(wine.aroma))
            }
        }

        private fun parseAromaToList(aroma: String): List<String> {
            val aromaList = mutableListOf<String>()

            // (\w+) 단어 문자를 포함하는 첫 번째 캡처 그룹
            // = 는 key와 value를 구분하는 구분자로 그냥 텍스트
            // \[ 대괄호의 시작점 찾기
            // ([^]]+) 대괄호 ]을 제외한 문자를 포함하는 두 번째 캡처 그룹, 대괄호 빼고 매칭됨
            // \] 대괄호의 종료점 찾기
            val aromaPairs = Regex("""(\w+)=\[([^]]+)]""").findAll(aroma)

            aromaPairs.forEach { matchResult ->
                val key = matchResult.groupValues[1]
                val values = matchResult.groupValues[2]
                values.split(", ").forEach { value ->
                    aromaList.add("$AROMA_PREFIX${key.trim()}:${value.trim()}")
                }
            }

            return aromaList
        }
    }
}

