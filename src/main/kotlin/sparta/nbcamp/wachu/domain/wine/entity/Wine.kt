package sparta.nbcamp.wachu.domain.wine.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(name = "wine")
class Wine(

    @Column(name = "name", length = 100, unique = true)
    val name: String,

    @Column(name = "sweetness")
    val sweetness: Int,

    @Column(name = "acidity")
    val acidity: Int,

    @Column(name = "body")
    val body: Int,

    @Column(name = "tannin")
    val tannin: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: Type = Type.UNDEFINED,

    @Column(name = "aroma")
    val aroma: String,

    @Column(name = "price")
    var price: Int?,

    @Column(name = "kind", length = 50)
    val kind: String?,

    @Column(name = "style", length = 50)
    val style: String?,

    @Column(name = "country", length = 50)
    val country: String?,

    @Column(name = "region", length = 50)
    val region: String?,

    @Lob
    @Column(name = "embedding")
    val embedding: String?,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

)