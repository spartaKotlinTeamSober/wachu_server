package sparta.nbcamp.wachu.domain.review.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import sparta.nbcamp.wachu.domain.wine.entity.Wine
import java.time.LocalDateTime

@Entity
@Table(name = "review")
class Review(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    val wine: Wine,

    @Column
    val memberId: Long,

    @Column(length = 100)
    val title: String,

    @Column
    val description: String,

    @Column
    val score: Double,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    fun hasPermission(memberId: Long, role: String): Boolean {
        return this.memberId == memberId || role == "ROLE_ADMIN"
    }
}