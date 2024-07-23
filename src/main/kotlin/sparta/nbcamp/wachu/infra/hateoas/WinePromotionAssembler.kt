package sparta.nbcamp.wachu.infra.hateoas

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.controller.WineController
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse

@Component
class WinePromotionAssembler : RepresentationModelAssembler<PromotionWineResponse, EntityModel<WinePromotionModel>> {

    override fun toModel(entity: PromotionWineResponse): EntityModel<WinePromotionModel> {
        val model = WinePromotionModel.from(entity)
        return EntityModel.of(model).apply {
            add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(WineController::class.java)
                        .getPromotionWineList(direction = "asc", sortBy = "price", page = 0, size = 10)
                ).withSelfRel()
            )
        }
    }
}