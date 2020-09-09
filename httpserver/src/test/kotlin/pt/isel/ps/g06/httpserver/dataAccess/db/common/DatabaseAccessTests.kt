package pt.isel.ps.g06.httpserver.dataAccess.db.common

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository

/**
 * Test class that tests common features of every Data Access Object (DAO)
 */
//TODO delete if not going to be used
/*
@SpringBootTest
class DatabaseAccessTests {
    @Autowired
    lateinit var mealDbRepository: MealDbRepository

    @Test
    fun `cached sequence result from database should close after iterated`() {
        val maxCount = 10
        val ingredients = mealDbRepository.getAllIngredients(0, maxCount)

        //Perform some basic mapping operations to check if value is there and valid
        val count = ingredients
                .map { it.meal_name }
                .count()

        Assert.assertEquals(maxCount, count)
    }

}*/