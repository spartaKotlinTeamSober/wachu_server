package sparta.nbcamp.wachu.infra.aws.s3

import org.springframework.context.ApplicationEvent

class S3CreateEvent(source: Any) : ApplicationEvent(source)