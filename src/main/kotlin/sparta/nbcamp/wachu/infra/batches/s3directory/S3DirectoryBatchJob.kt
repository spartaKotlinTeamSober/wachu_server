package sparta.nbcamp.wachu.infra.batches.s3directory

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.aws.s3.S3Service

@Component
class S3DirectoryBatchJob(
    private val s3Service: S3Service,
    private val inMemoryDirectory: InMemoryDirectory
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        job()
    }

    @Scheduled(cron = "0 0 * * * *")
    fun job() {
        WineType.entries.forEach {
            s3Service.getImage(S3FilePath.WINE.path + it.path)
                .also { element ->
                    println(element.toString())
                    inMemoryDirectory.set(it, element)
                }
        }
    }
}