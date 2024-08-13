package sparta.nbcamp.wachu.infra.batches.s3directory

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.aws.s3.S3Service

@Component
class S3DirectoryBatchJob(
    private val s3Service: S3Service,
    private val inMemoryDirectory: InMemoryDirectory
) {
    @PostConstruct
    fun init() {
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