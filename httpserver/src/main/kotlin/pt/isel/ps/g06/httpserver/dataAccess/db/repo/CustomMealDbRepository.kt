package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CustomMealDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCustomMealDto
import java.util.stream.Stream

class CustomMealDbRepository(private val databaseContext: DatabaseContext) {

    fun getAllFromUser(submitterId: Int): Stream<DbCustomMealDto> {
        return databaseContext.inTransaction {
            it.attach(CustomMealDao::class.java).getAllUserCustomMeals(submitterId)
        }
    }

    fun getCustomMealFromUser(submitterId: Int, mealName: String): DbCustomMealDto {
        return databaseContext.inTransaction {
            it.attach(CustomMealDao::class.java).getUserCustomMeal(submitterId, mealName)
        }
    }

    fun insertUserCustomMeal(
            submissionId: Int,
            mealName: String,
            mealPortion: Int,
            carbAmount: Int,
            imageUrl: String
    ): DbCustomMealDto {
        return databaseContext.inTransaction {
            it.attach(CustomMealDao::class.java).insertUserCustomMeal(submissionId, mealName, mealPortion, carbAmount, imageUrl)
        }
    }

    fun deleteUserCustomMeal(mealName: String): DbCustomMealDto {
        return databaseContext.inTransaction {
            it.attach(CustomMealDao::class.java).deleteUserCustomMeal(mealName)
        }
    }
}