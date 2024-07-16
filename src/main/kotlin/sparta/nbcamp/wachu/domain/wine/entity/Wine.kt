package sparta.nbcamp.wachu.domain.wine.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "wine")
class Wine(

    @Column(name = "name")
    val name: String,

    @Column(name = "sweetness")
    val sweetness: Int,

    @Column(name = "acidity")
    val acidity: Int,

    @Column(name = "body")
    val body: Int,

    @Column(name = "tannin")
    val tannin: Int,

    @Column(name = "type")
    val type: String,

    @Column(name = "aroma")
    val aroma: String,

    @Column(name = "price")
    var price: Int?,

    @Column(name = "kind")
    val kind: String?,

    @Column(name = "style")
    val style: String?,

    @Column(name = "country")
    val country: String?,

    @Column(name = "region")
    val region: String?,

    @Column(name = "embedding")
    val embedding: String?, // 임시로 String 타입 명시

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

)