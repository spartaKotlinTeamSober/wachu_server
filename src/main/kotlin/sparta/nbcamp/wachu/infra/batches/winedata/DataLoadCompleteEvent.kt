package sparta.nbcamp.wachu.infra.batches.winedata

import org.springframework.context.ApplicationEvent

class DataLoadCompleteEvent(source: Any) : ApplicationEvent(source)