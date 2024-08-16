package sparta.nbcamp.wachu.infra.media

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.wine.entity.WineType
import sparta.nbcamp.wachu.domain.wine.service.WineImageGetter
import sparta.nbcamp.wachu.infra.aws.s3.S3Service
import sparta.nbcamp.wachu.infra.batches.s3directory.InMemoryDirectory
import sparta.nbcamp.wachu.infra.tika.TikaUtil
import java.net.URL

@Service
class MediaS3ServiceImpl(
    private val s3Service: S3Service,
    private val tikaUtil: TikaUtil,
    private val inMemoryDirectory: InMemoryDirectory
) : MediaS3Service {
    private val logger = LoggerFactory.getLogger(MediaS3Service::class.java)
    override fun upload(file: MultipartFile, filePath: String): String {
        if (!tikaUtil.validateMediaFile(file)) {
            throw IllegalArgumentException("Invalid file format")
        }
        logger.info("upload file: $filePath")
        return s3Service.upload(file, filePath)
    }

    override fun upload(fileList: List<MultipartFile>, filePath: String): List<String> {
        if (!fileList.all { tikaUtil.validateMediaFile(it) }) {
            throw IllegalArgumentException("Invalid file format")
        }//하나라도 이상하면 전체 취소
        //없으면 전체 upload
        logger.info("upload files: $filePath")
        return fileList.map { s3Service.upload(it, filePath) }
    }

    override fun getS3Image(filePath: String): MutableList<String> {
        //경로 안에 모든 파일을 불러옴
        return s3Service.getImage(filePath).toMutableList()
    }

    override fun getPresignedUrl(file: MultipartFile, filePath: String): URL {
        if (!tikaUtil.validateMediaFile(file)) {
            throw IllegalArgumentException("Invalid file format")
        }
        logger.info("upload file: $filePath")
        return s3Service.generatePresignedUrl(file, filePath)
    }

    override fun getInMemoryDirectory(type: WineType): MutableList<String> {
        return inMemoryDirectory.get(type)!!.toMutableList()
    }

    @PostConstruct
    fun init() {
        WineImageGetter.init(this)
    }
}