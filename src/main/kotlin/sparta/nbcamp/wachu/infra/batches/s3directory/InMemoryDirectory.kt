package sparta.nbcamp.wachu.infra.batches.s3directory

import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.entity.WineType

@Component
class InMemoryDirectory {
    private val directory = HashMap<WineType, List<String>>()

    fun set(key: WineType, values: List<String>) {
        synchronized(this) {
            directory[key] = values
        }
    }

    fun get(key: WineType): List<String>? {
        return synchronized(this) {
            directory[key]
        }
    }
}