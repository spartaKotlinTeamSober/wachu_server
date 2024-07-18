package sparta.nbcamp.wachu.domain.review.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "review")
class Review(
    @Column
    val wineId: Long,

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