package sparta.nbcamp.wachu.infra.batches.winedata

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.repository.WineRepository

@Component
class WineDataBatchJob(
    private val wineRepository: WineRepository,
    private val inMemoryCache: InMemoryCache,
    private val eventPublisher: ApplicationEventPublisher
) : ApplicationListener<ContextRefreshedEvent> {

    @Async
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val wines = wineRepository.findAll()
        inMemoryCache.loadWines(wines)
        eventPublisher.publishEvent(DataLoadCompleteEvent(this))
    }
}