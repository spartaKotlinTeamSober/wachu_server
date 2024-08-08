package sparta.nbcamp.wachu.domain.testH2db

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class H2DatabaseConnectionTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun testH2DatabaseConnection() {
        val url = jdbcTemplate.dataSource?.connection?.metaData?.url

        url shouldContain "jdbc:h2"
    }
}