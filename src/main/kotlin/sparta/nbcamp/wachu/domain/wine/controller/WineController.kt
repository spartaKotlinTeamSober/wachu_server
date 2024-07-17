package sparta.nbcamp.wachu.domain.wine.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.wine.dto.RecommendWineRequest
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import sparta.nbcamp.wachu.domain.wine.service.WineService

@RequestMapping("/api/v1/wines")
@RestController
class WineController(
    private val wineService: WineService,
) {

    @GetMapping()
    fun getWineList(
        @RequestParam(value = "query", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "id") sortBy: String,
        @RequestParam("sort_direction", defaultValue = "desc") direction: String,
    ): ResponseEntity<List<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(
            wineService.getWineList(
                query = query, page = page, size = size, sortBy = sortBy, direction = direction
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
    fun getPopularWineList(
        @PageableDefault(
            page = 0,
            size = 5,
            sort = ["미정?"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable //TODO() sort가 필요한가?
    ): ResponseEntity<List<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(wineService.getPopularWineList(pageable = pageable))
    }

    @GetMapping("/recommend")
    fun recommendWine(@RequestBody request: RecommendWineRequest): ResponseEntity<List<WineResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(wineService.recommendWine(request = request))
    }

    @PostMapping
    fun postWineForTest(@RequestBody request: WineResponse) {
        return wineService.postWineForTest(request)
    }
}