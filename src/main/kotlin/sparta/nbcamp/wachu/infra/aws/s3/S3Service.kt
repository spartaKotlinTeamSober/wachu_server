package sparta.nbcamp.wachu.infra.aws.s3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.IOException
import java.net.URL
import java.time.Duration

@Service
class S3Service @Autowired constructor(
    private val s3client: S3Client,
    private val s3Presigner: S3Presigner
) {
    @Value("\${aws.s3.bucket}")
    private lateinit var bucket: String

    @Throws(IOException::class)
    fun upload(file: MultipartFile, filePath: String): String {

        val keyName = filePath + file.originalFilename // S3에 저장할 파일 경로 및 이름

        // 파일을 S3에 업로드하는 요청 설정
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .build()

        // S3에 파일 업로드
        s3client.putObject(putObjectRequest, RequestBody.fromInputStream(file.inputStream, file.size))

        // 업로드된 파일의 CDN URL 반환
        return "https://cdn.sober-wachu.com/$keyName"
    }

    fun getImage(filePath: String): List<String> {
        val imageList = s3client.listObjectsV2(
            ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(filePath)
                .build()
        )
        return imageList.contents().map {
            "https://cdn.sober-wachu.com/${it.key()}"
        }
    }

    fun generatePresignedUrl(
        file: MultipartFile,
        filePath: String,
        expirationMinutes: Long = 10 // URL 만료 시간. 별도 설정 없을 시 기본 시간 적용됨
    ): URL {

        val keyName = filePath + file.originalFilename // S3에 저장할 파일 경로 및 이름

        // 파일을 S3에 업로드하는 요청 설정
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .build()

        // 프리사인드 요청 생성
        val presignRequest = PutObjectPresignRequest.builder()
            .putObjectRequest(putObjectRequest)
            .signatureDuration(Duration.ofMinutes(expirationMinutes))
            .build()

        // 프리사인드 URL 생성
        val presignedUrl = s3Presigner.presignPutObject(presignRequest).url()

        return presignedUrl
    }
}