package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.util.ClosableSequence
import pt.isel.ps.g06.httpserver.util.asClosableSequence
import pt.isel.ps.g06.httpserver.util.emptyClosableSequence

private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(private val databaseContext: DatabaseContext) {

    fun getAll(skip: Int?, count: Int?): ClosableSequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAll(skip, count)
                    .asClosableSequence()
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Sequence<String>): ClosableSequence<DbCuisineDto> {
        val cuisines = cuisineIds.toList()
        //TODO See if eager call is possible to avoid
        if (cuisines.isEmpty()) return emptyClosableSequence()

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisines)
                    .asClosableSequence()
        }
    }

    fun getAllByNames(cuisineNames: Sequence<String>): ClosableSequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByNames(cuisineNames.toList())       //TODO See if eager call is possible to avoid
                    .asClosableSequence()
        }
    }


    fun getAllByMealId(mealId: Int): ClosableSequence<DbCuisineDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(CuisineDao::class.java)
                    .getByMealId(mealId)
                    .asClosableSequence()
        }
    }

    fun getAllByRestaurantId(restaurantId: Int): ClosableSequence<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(CuisineDao::class.java)
                    .getAllByRestaurantId(restaurantId)
                    .asClosableSequence()
        }
    }
}