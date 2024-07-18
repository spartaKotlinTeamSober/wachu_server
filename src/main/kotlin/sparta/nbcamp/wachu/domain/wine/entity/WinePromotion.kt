package sparta.nbcamp.wachu.domain.wine.entity

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
import java.time.LocalDateTime

@Entity
@Table(name = "wine_promotion")
class WinePromotion(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    var wine: Wine,

    @Column(name = "status")
    var status: PromotionStatus,

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @CreationTimestamp
    @Column(name = "opened_at")
    val openedAt: LocalDateTime,

    @CreationTimestamp
    @Column(name = "closedAt")
    val closedAt: LocalDateTime,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)