package sparta.nbcamp.wachu.domain.admin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.admin.dto.DesignatePromotionRequest
import sparta.nbcamp.wachu.domain.admin.service.AdminService
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse

@RequestMapping("/admin")
@RestController
class AdminController(
    private val adminService: AdminService,
) {

    @PostMapping("/promotion")
    fun designatePromotion(@RequestBody request: DesignatePromotionRequest): ResponseEntity<PromotionWineResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.designatePromotion(request = request))
    }
}