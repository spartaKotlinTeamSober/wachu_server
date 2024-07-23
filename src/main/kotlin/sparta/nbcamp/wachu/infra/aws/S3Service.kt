package sparta.nbcamp.wachu.infra.aws

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.IOException

@Service
class S3Service @Autowired constructor(
    private val credentials: AwsBasicCredentials,
) {
    @Value("\${aws.s3.bucket}")
    private lateinit var bucket: String

    @Throws(IOException::class)
    fun upload(file: MultipartFile, filePath: String): String {

        val s3 = S3Client.builder()
            .region(Region.AP_NORTHEAST_2) // 원하는 지역으로 설정
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()

        val keyName = filePath + file.originalFilename // S3에 저장할 파일 경로 및 이름

        // 파일을 S3에 업로드하는 요청 설정
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .build()

        // S3에 파일 업로드
        s3.putObject(putObjectRequest, RequestBody.fromInputStream(file.inputStream, file.size))

        // 업로드된 파일의 CDN URL 반환
        return "https://d3i8tzom3e0at0.cloudfront.net/$keyName"
    }
}