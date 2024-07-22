package sparta.nbcamp.wachu.infra.hateoas

import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.wine.controller.WineController
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

@Component
class WinePromotionAssembler : RepresentationModelAssembler<WinePromotion, WinePromotionModel> {

    override fun toModel(entity: WinePromotion): WinePromotionModel {
        val model = WinePromotionModel(entity)
        model.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(WineController::class.java)
                    .getPromotionWineList(direction = "asc", sortBy = "price", page = 0, size = 10)
            ).withSelfRel()
        )
        return model
    }
}