package sparta.nbcamp.wachu.infra.media

import org.springframework.web.multipart.MultipartFile

interface MediaService{
    fun upload(file: MultipartFile, filePath: String):String
}