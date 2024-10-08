package sparta.nbcamp.wachu.infra.aws.s3

enum class S3FilePath(val path: String) {
    PAIRING("pairing/"),
    PROFILE("profile/"),
    REVIEW("review/"),
    WINE("wine/")
}