package pt.isel.ps.g06.httpserver.common.nutrition

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

class CarbToolsTest {

    @Test
    fun `carb tool should provide correct values`() {
        val result = calculateCarbsFromBase(100, 10, 200)
        Assert.assertEquals(20F, result)
    }
}