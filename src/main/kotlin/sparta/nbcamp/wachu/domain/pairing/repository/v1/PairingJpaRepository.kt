package sparta.nbcamp.wachu.domain.pairing.repository.v1

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

interface PairingJpaRepository : JpaRepository<Pairing, Long>