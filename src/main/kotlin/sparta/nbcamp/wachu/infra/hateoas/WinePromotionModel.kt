package sparta.nbcamp.wachu.infra.hateoas

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.core.Relation
import sparta.nbcamp.wachu.domain.wine.entity.WinePromotion

@Relation(itemRelation = "winePromotion", collectionRelation = "winePromotions")
class WinePromotionModel(winePromotion: WinePromotion) : EntityModel<WinePromotion>(winePromotion)