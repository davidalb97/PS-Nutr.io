package pt.isel.ps.g06.httpserver.dataAccess.meal.database.repos

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import java.lang.Exception

@Repository
class MealRepository(private val jdbi: Jdbi) {

    fun newMeal(
            submitterId: Int,
            mealName: String,
            cuisine: String,
            ingredientIds: Array<Int> = emptyArray(),
            restaurantId: Int? = null
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(TransactionIsolationLevel.SERIALIZABLE) {

                /*
                val mealDao = it.attach(MealDao::class.java)
                val meals = mealDao.getMealsByName(mealName)

                var targetMealID: Int? = null

                //If a meal must be inserted
                if(ingredientIds.isNotEmpty() || meals.isEmpty() || ) {
                    targetMealID = mealDao.insertMeal(mealName)
                } else {
                    targetMealID = meals.
                }

                //Is submission
                if(restaurantId != null) {
                    //Restaurant must exist
                    if(it.attach(RestaurantDao::class.java).getRestaurantById(restaurantId) == null) {
                        throw MissingRestaurantException(restaurantId)
                    }


                }
                */
                true
            }
        }
    }
}