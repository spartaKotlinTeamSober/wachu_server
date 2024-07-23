package sparta.nbcamp.wachu.infra.hateoas

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.core.Relation
import sparta.nbcamp.wachu.domain.wine.dto.PromotionWineResponse

@Relation(itemRelation = "winePromotion", collectionRelation = "winePromotions")
class WinePromotionModel(promotionWineResponse: PromotionWineResponse) : EntityModel<PromotionWineResponse>(promotionWineResponse) {

    companion object {
        fun from(promotionWineResponse: PromotionWineResponse): WinePromotionModel {
            return WinePromotionModel(promotionWineResponse)
        }
    }
}
