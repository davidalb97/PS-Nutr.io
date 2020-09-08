package pt.isel.ps.g06.httpserver.dataAccess.db.common

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository

/**
 * Test class that tests common features of every Data Access Object (DAO)
 */
class DatabaseAccessTests {
    @Autowired
    lateinit var mealDbRepository: MealDbRepository

    @Test
    fun `stream result from database should not close after repository call`() {
        val maxCount = 10
        val ingredients = mealDbRepository.getAllIngredients(0, null)

        //Perform some basic mapping operations to check if value is there and valid
        val count = ingredients
                .map { it.meal_name }
                .count()

        Assert.assertEquals(maxCount, count)
    }
}