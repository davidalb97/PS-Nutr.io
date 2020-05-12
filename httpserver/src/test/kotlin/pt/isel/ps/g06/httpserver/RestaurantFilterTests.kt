package pt.isel.ps.g06.httpserver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pt.isel.ps.g06.httpserver.model.Restaurant

class RestaurantFilterTests {

    @Test
    fun shouldFilterIgnoringCase() {
        /*
        val expected = Restaurant(2, "Other place", 20F, 20F, listOf("Portuguese", "Good Eats"))

        val restaurants = setOf(
                Restaurant(1, "The place", 10F, 10F, listOf("Sushi", "Japanese")),
                expected
        )

        val result = RestaurantFilter(restaurants).filter(listOf("SuShi"))

        Assertions.assertEquals(result.size, 1)
        Assertions.assertEquals(result.first(), expected)
         */
        TODO("Commented compilation errors")
    }

    @Test
    fun shouldNotFilterIfParametersAreEmpty() {
        /*
        val restaurants = setOf(
                Restaurant(1, "The place", 10F, 10F, listOf("Sushi", "Japanese")),
                Restaurant(2, "Other place", 20F, 20F, listOf("Portuguese", "Good Eats"))
        )

        val result = RestaurantFilter(restaurants).filter(emptyList(), emptyList())

        Assertions.assertEquals(result.size, restaurants.size)
         */
        TODO("Commented compilation errors")
    }
}