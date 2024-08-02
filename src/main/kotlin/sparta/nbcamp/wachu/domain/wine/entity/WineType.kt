package sparta.nbcamp.wachu.domain.wine.entity

enum class WineType(val path: String) {
    RED("red/"),
    WHITE("white/"),
    SPARKLING("sparkling/"),
    ROSE("rose/"),
    FORTIFIED("fortified/"),
    UNDEFINED("undefined/"),
}