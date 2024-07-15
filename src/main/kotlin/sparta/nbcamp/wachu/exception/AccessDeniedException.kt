package sparta.nbcamp.wachu.exception

data class AccessDeniedException(
    private val text: String
) : SecurityException(
    "Access Denied: $text"
)