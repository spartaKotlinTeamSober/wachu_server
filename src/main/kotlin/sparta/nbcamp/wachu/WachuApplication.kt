package sparta.nbcamp.wachu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @ServletComponentScan
@SpringBootApplication
class WachuApplication

fun main(args: Array<String>) {
    runApplication<WachuApplication>(*args)
}
