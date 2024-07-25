package sparta.nbcamp.wachu.infra.media

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.infra.media.apacheTika.TikaService
import sparta.nbcamp.wachu.infra.media.aws.S3Service

@Service
class MediaServiceImpl(
    private val s3Service: S3Service,
    private val tikaService: TikaService
) : MediaService {
    override fun upload(file: MultipartFile, filePath: String): String {
        if (!tikaService.validateMediaFile(file)) {
            throw IllegalArgumentException("Invalid file format")
        }
        return s3Service.upload(file, filePath)
    }

    override fun upload(fileList: List<MultipartFile>, filePath: String): List<String> {
        if (!fileList.all { tikaService.validateMediaFile(it) }) {
            throw IllegalArgumentException("Invalid file format")
        }//하나라도 이상하면 전체 취소
        //없으면 전체 upload
        return fileList.map { s3Service.upload(it, filePath) }
    }
}