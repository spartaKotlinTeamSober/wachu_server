package sparta.nbcamp.wachu.infra.batches.winedata

import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.entity.Wine

@Component
class InMemoryCache {
    private val wines = mutableListOf<Wine>()

    fun loadWines(newWines: List<Wine>) {
        synchronized(this) {
            wines.clear()
            wines.addAll(newWines)
        }
    }

    fun getWines(): List<Wine> {
        return synchronized(this) {
            wines.toList()
        }
    }

    fun getWine(id: Long): Wine? {
        return synchronized(this) {
            wines.find { it.id == id }
        }
    }
}