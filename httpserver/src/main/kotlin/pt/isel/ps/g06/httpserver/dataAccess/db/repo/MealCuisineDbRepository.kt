package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.MealCuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val mealCuisineDaoClass = MealCuisineDao::class.java

@Repository
class MealCuisineDbRepository(private val jdbi: Jdbi) {

    fun getByHereCuisinesIdentifiers(cuisines: Collection<String>): Collection<MealCuisineDto> {
        return jdbi.inTransaction<Collection<MealCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealCuisineDaoClass).getByHereCuisines(cuisines)
        }
    }

    fun getMealsForCuisines(cuisines: Collection<String>): Collection<MealCuisineDto> {
        return jdbi.inTransaction<Collection<MealCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(mealCuisineDaoClass).getByCuisines(cuisines)
        }
    }
}