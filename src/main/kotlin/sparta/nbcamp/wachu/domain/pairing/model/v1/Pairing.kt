package sparta.nbcamp.wachu.domain.pairing.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedBy
import java.time.LocalDateTime

@Entity(name = "pairing")
class Pairing(
    @Column
    val wineId: Long,

    @Column
    val memberId: Long,

    @Column
    val title: String,

    @Column
    val description: String,

    @Column
    val photoUrl: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column(nullable = true, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @CreatedBy
    @Column
    val createdBy: Long = 0
}