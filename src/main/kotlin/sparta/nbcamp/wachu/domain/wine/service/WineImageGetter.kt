package sparta.nbcamp.wachu.domain.wine.service

import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.infra.media.MediaS3Service

object WineImageGetter {
    private lateinit var mediaS3Service: MediaS3Service

    fun getWineImage(wineType: WineType, wineId: Long): String {
        val list = mediaS3Service.getInMemoryDirectory(wineType)
        return if (list.isNotEmpty()) {
            list.removeFirst()//첫번째 원소는 파일경로만 있어서 제외
            list[list.size % wineId.toInt()]
        } else ""
    }

    fun init(mediaS3Service: MediaS3Service) {
        this.mediaS3Service = mediaS3Service
    }
}