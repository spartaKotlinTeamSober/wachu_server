package sparta.nbcamp.wachu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication
class WachuApplication

fun main(args: Array<String>) {
    runApplication<WachuApplication>(*args)
}
