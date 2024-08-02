package sparta.nbcamp.wachu.domain.wine.service

import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.infra.aws.s3.S3FilePath
import sparta.nbcamp.wachu.infra.media.MediaS3Service

object WineImageGetter {
    private lateinit var mediaS3Service: MediaS3Service

    fun getWineImage(wineType: WineType): String {
        val list = mediaS3Service.getS3Image(S3FilePath.WINE.path + wineType.path)
        list.removeFirst()//첫번째 원소는 파일경로만 있어서 제외
        return list.random()
    }

    fun init(mediaS3Service: MediaS3Service) {
        this.mediaS3Service = mediaS3Service
    }
}