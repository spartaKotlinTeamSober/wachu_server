package sparta.nbcamp.wachu.infra.openai.service

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.infra.batches.winedata.DataLoadCompleteEvent

@Component
class WineEventListener {
    private var dataLoaded: Boolean = false

    @EventListener
    fun onDataLoadComplete(event: DataLoadCompleteEvent) {
        dataLoaded = true
    }

    fun isLoaded() = dataLoaded
}