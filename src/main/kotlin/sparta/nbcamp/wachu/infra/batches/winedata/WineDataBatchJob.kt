package sparta.nbcamp.wachu.infra.batches.winedata

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository

@Component
class WineDataBatchJob(
    private val wineRepository: WineRepository,
    private val inMemoryCache: InMemoryCache,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Async
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationEvent(event: ApplicationReadyEvent) {
        val wines = wineRepository.findAll()
        inMemoryCache.loadWines(wines)
        eventPublisher.publishEvent(DataLoadCompleteEvent(this))
    }
}