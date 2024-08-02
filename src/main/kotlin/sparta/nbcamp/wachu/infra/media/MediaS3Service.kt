package sparta.nbcamp.wachu.infra.media

import org.springframework.web.multipart.MultipartFile

interface MediaS3Service {
    fun upload(file: MultipartFile, filePath: String): String
    fun upload(fileList: List<MultipartFile>, filePath: String): List<String>
    fun getS3Image(filePath: String): MutableList<String>
}