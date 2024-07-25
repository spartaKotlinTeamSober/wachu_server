package sparta.nbcamp.wachu.infra.media.apacheTika

import org.apache.tika.Tika
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Locale

@Service
class TikaService {
    private fun detectMimeType(file: MultipartFile): String {
        val tika = Tika()
        return file.inputStream.use { tika.detect(it) }
    }

    fun validateMediaFile(file: MultipartFile, isTypeMatch: Boolean = false): Boolean {
        val mimeTypeByExtension = mapOf(
            "jpeg" to "image/jpeg",
            "jpg" to "image/jpeg",
            "png" to "image/png",
            "webp" to "image/webp"
        )

        // 실제 MIME 타입 감지
        val actualMimeType = detectMimeType(file).lowercase(Locale.getDefault())
        // 파일 확장자 추출
        val originFileType = file.originalFilename
            ?.substringAfterLast(".")
            ?.lowercase(Locale.getDefault())

        // 기대되는 MIME 타입
        val expectedMimeType = mimeTypeByExtension[originFileType]

        return if (isTypeMatch) {
            // MIME 타입이 기대하는 타입과 일치하고, 파일 확장자가 기대되는 MIME 타입과 일치하는지 확인
            expectedMimeType != null && actualMimeType == expectedMimeType
        } else {
            // MIME 타입이 허용된 타입 목록에 포함되는지 확인
            expectedMimeType != null && mimeTypeByExtension.values.contains(actualMimeType)
        }
    }
}