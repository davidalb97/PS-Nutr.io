package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.util.asCachedSequence

private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(private val databaseContext: DatabaseContext) {
    fun getAll(skip: Int?, count: Int?): Sequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAll(skip, count)
                    .asCachedSequence()
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Sequence<String>): Sequence<DbCuisineDto> {
        val cuisines = cuisineIds.toList()
        //TODO See if eager call is possible to avoid
        if (cuisines.isEmpty()) return emptySequence()

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisines)
                    .asCachedSequence()
        }
    }

    fun getAllByNames(cuisineNames: Sequence<String>): Sequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByNames(cuisineNames.toList())       //TODO See if eager call is possible to avoid
                    .asCachedSequence()
        }
    }


    fun getAllByMealId(mealId: Int): Sequence<DbCuisineDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(CuisineDao::class.java)
                    .getByMealId(mealId)
                    .asCachedSequence()
        }
    }

    fun getAllByRestaurantId(restaurantId: Int): Sequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(CuisineDao::class.java)
                    .getAllByRestaurantId(restaurantId)
                    .asCachedSequence()
        }
    }
}