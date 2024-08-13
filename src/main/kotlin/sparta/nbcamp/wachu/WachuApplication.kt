package sparta.nbcamp.wachu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WachuApplication

fun main(args: Array<String>) {
    runApplication<WachuApplication>(*args)
}
