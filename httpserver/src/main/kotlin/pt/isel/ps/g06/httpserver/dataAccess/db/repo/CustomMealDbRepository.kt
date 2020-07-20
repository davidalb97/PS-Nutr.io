package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CustomMealDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCustomMealDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val cuisineDaoClass = CustomMealDao::class.java

class CustomMealDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAllFromUser(submitterId: Int): Sequence<DbCustomMealDto> {
        return jdbi.inTransaction<Sequence<DbCustomMealDto>, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAllUserCustomMeals(submitterId)
        }
    }

    fun getCustomMealFromUser(submitterId: Int, mealName: String): DbCustomMealDto {
        return jdbi.inTransaction<DbCustomMealDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getUserCustomMeal(submitterId, mealName)
        }
    }

    fun insertUserCustomMeal(
            submissionId: Int,
            mealName: String,
            mealPortion: Int,
            carbAmount: Int,
            imageUrl: String
    ): DbCustomMealDto {
        return jdbi.inTransaction<DbCustomMealDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .insertUserCustomMeal(submissionId, mealName, mealPortion, carbAmount, imageUrl)
        }
    }

    fun deleteUserCustomMeal(mealName: String): DbCustomMealDto {
        return jdbi.inTransaction<DbCustomMealDto, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .deleteUserCustomMeal(mealName)
        }
    }
}