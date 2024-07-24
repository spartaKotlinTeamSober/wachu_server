package sparta.nbcamp.wachu.infra.apaheTika

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

    fun validateMediaFile(file: MultipartFile, isTypeMatch:Boolean=false): Boolean {
        val mimeTypeByExtension = hashSetOf("image/jpeg", "image/jpg", "image/webp", "image/png")
        val actualMimeType = detectMimeType(file)
            .lowercase(Locale.getDefault())
        val originFileType = file.originalFilename
            ?.substringAfterLast(".")
            ?.lowercase(Locale.getDefault())

        val result =
            if (isTypeMatch)
                mimeTypeByExtension.contains(actualMimeType)
                    && actualMimeType.substringAfterLast("/") == originFileType
            else
                mimeTypeByExtension.contains(actualMimeType)
        return result
    }
}