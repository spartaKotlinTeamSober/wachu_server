package sparta.nbcamp.wachu.domain.wine.controller

import org.springframework.data.domain.Page
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion
import sparta.nbcamp.wachu.domain.wine.service.WineService
import sparta.nbcamp.wachu.infra.hateoas.WinePromotionModel

@RequestMapping("/api/v1/wines")
@RestController
class WineController(
    private val wineService: WineService,
) {

    @GetMapping()
    fun getWineList(
        @RequestParam(value = "query", defaultValue = "") query: String,
        @RequestParam(value = "price") price: Int?,
        @RequestParam(value = "acidity") acidity: List<Int>?,
        @RequestParam(value = "body") body: List<Int>?,
        @RequestParam(value = "sweetness") sweetness: List<Int>?,
        @RequestParam(value = "tannin") tannin: List<Int>?,
        @RequestParam(value = "type") type: String?,

        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int,
        @RequestParam(value = "sort_by", defaultValue = "id") sortBy: String,
        @RequestParam(value = "sort_direction", defaultValue = "desc") direction: String,
    ): ResponseEntity<Page<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(
            wineService.getWineList(
                query = query, price = price,
                acidity = acidity,
                body = body,
                sweetness = sweetness,
                tannin = tannin,
                type = type, page = page, size = size, sortBy = sortBy, direction = direction,
            )
        )
    }

    @GetMapping("/{wineId}")
    fun getWineById(@PathVariable wineId: Long): ResponseEntity<WineResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(wineService.getWineById(wineId = wineId))
    }

    @GetMapping("/comparison")
    fun compareWine(@RequestParam("wineId") wineIds: List<Long>): ResponseEntity<List<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(wineService.compareWine(wineIds = wineIds))
    }

    @GetMapping("/promotion")
    fun getPromotionWineList(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int,
        @RequestParam(value = "sort_by", defaultValue = "createdAt") sortBy: String,
        @RequestParam(value = "sort_direction", defaultValue = "asc") direction: String,
    ): ResponseEntity<Page<WinePromotion>> {
        return ResponseEntity.status(HttpStatus.OK).body(
            wineService.getPromotionWineList(
                page = page,
                size = size,
                sortBy = sortBy,
                direction = direction
            )
        )
    }

    @GetMapping("/recommend")
    fun recommendWine(@RequestBody request: RecommendWineRequest): ResponseEntity<List<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(wineService.recommendWine(request = request))
    }
}