package sparta.nbcamp.wachu.infra.aws

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class AwsConfig {
    @Value("\${aws.s3.accessKey}")
    private val accessKey: String? = null

    @Value("\${aws.s3.secretKey}")
    private val secretKey: String? = null

    @Bean
    fun awsBasicCredentials(): S3Client {
        //S3 엑세스 config
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2) // 원하는 지역으로 설정
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}