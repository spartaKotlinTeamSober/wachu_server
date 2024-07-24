package sparta.nbcamp.wachu.infra.media.aws

enum class S3FilePath(val path: String) {
    PAIRING("pairing/"),
    PROFILE("profile/"),
    REVIEW("review/")
}