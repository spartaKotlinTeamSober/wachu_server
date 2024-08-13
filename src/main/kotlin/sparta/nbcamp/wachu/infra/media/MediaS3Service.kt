package sparta.nbcamp.wachu.infra.media

import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.wine.entity.WineType

interface MediaS3Service {
    fun upload(file: MultipartFile, filePath: String): String
    fun upload(fileList: List<MultipartFile>, filePath: String): List<String>
    fun getS3Image(filePath: String): MutableList<String>
    fun getInMemoryDirectory(type: WineType): MutableList<String>
}