package sparta.nbcamp.wachu.domain.pairing.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity(name = "pairing")
class Pairing(
    @Column
    val wineId: Long,

    @Column
    val memberId: Long,

    @Column(length = 100)
    val title: String,

    @Column(length = 255)
    val description: String,

    @Column
    val photoUrl: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    fun hasPermission(memberId: Long, role: String): Boolean {
        return this.memberId == memberId || role == "ROLE_ADMIN"
    }
}