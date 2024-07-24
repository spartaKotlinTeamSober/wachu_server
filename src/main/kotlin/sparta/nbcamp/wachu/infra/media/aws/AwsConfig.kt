package sparta.nbcamp.wachu.infra.media.aws

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials

@Configuration
class AwsConfig {
    @Value("\${aws.s3.accessKey}")
    private val accessKey: String? = null

    @Value("\${aws.s3.secretKey}")
    private val secretKey: String? = null

    @Bean
    fun awsBasicCredentials(): AwsBasicCredentials {
        //S3 엑세스 config
        return AwsBasicCredentials.create(accessKey, secretKey)
    }
}