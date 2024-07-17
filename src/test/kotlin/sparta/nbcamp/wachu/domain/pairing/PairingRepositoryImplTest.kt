package sparta.nbcamp.wachu.domain.pairing

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingJpaRepository
import sparta.nbcamp.wachu.domain.pairing.repository.v1.PairingRepositoryImpl
import java.util.Optional

class PairingRepositoryImplTest : BehaviorSpec({

    val pairingJpaRepository: PairingJpaRepository = mockk()
    val pairingRepositoryImpl: PairingRepositoryImpl = mockk()

    afterContainer {
        clearAllMocks()
    }

    fun generatePairings(count: Int): List<Pairing> {
        return List(count) {
            Pairing(
                wineId = 1L,
                memberId = 1L,
                title = "test",
                description = "test",
                photoUrl = "test"
            )
        }
    }


    given("PairingRepositoryImpl 에서") {

        `when`("존재하는 아이디로 findById 하면") {
            val pairing = Pairing(
                wineId = 1L,
                memberId = 1L,
                title = "test",
                description = "test",
                photoUrl = "test"
            )
            every { pairingJpaRepository.findById(1L) } returns Optional.of(pairing)

            then("pairing 을 반환한다") {
                val result = pairingRepositoryImpl.findById(1L)
                result shouldBe pairing
                verify { pairingJpaRepository.findById(1L) }
            }
        }

        `when`("존재하지 않는 아이디로 findById 하면") {
            every { pairingJpaRepository.findById(1L) } returns Optional.empty()

            then("null 을 반환한다") {
                val result = pairingRepositoryImpl.findById(1L)
                result shouldBe null
                verify { pairingJpaRepository.findById(1L) }
            }
        }

        `when`("findAll 하면") {
            val pairings = generatePairings((1..10).random())
            every { pairingJpaRepository.findAll() } returns pairings

            then("pairings 의 목록을 반환한다") {
                val result = pairingRepositoryImpl.findAll()
                result shouldBe pairings
                result.size shouldBe pairings.size
                verify { pairingJpaRepository.findAll() }
            }
        }

        `when`("pairing 을 save 하면") {
            val pairing = Pairing(
                wineId = 1L,
                memberId = 1L,
                title = "test",
                description = "test",
                photoUrl = "test"
            )
            every { pairingJpaRepository.save(pairing) } returns pairing

            then("pairing 이 save 되고, pairing 을 반환한다") {
                val result = pairingRepositoryImpl.save(pairing)
                result shouldBe pairing
                verify { pairingJpaRepository.save(pairing) }
            }
        }

        `when`("pairing 을 delete 하면") {
            val pairing = Pairing(
                wineId = 1L,
                memberId = 1L,
                title = "test",
                description = "test",
                photoUrl = "test"
            )
            every { pairingJpaRepository.delete(pairing) } just Runs

            then("pairing 이 delete 된다.") {
                pairingRepositoryImpl.delete(pairing)
                verify { pairingJpaRepository.delete(pairing) }
            }
        }
    }
})