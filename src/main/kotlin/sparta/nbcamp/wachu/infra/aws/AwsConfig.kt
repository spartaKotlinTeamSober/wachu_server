package sparta.nbcamp.wachu.infra.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
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
      return AwsBasicCredentials.create(accessKey, secretKey)
    }
}