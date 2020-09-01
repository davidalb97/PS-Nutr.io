package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import java.util.stream.Stream
import kotlin.streams.toList

private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(private val databaseContext: DatabaseContext) {
    fun getAll(skip: Int?, count: Int?): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAll(skip, count)
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Sequence<String>): Stream<DbCuisineDto> {
        val cuisines = cuisineIds.toList()
        //TODO See if eager call is possible to avoid
        if (cuisines.isEmpty()) return Stream.empty()

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisines)
        }
    }

    fun getAllByNames(cuisineNames: Stream<String>): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByNames(cuisineNames.toList())       //TODO See if eager call is possible to avoid
        }
    }


    fun getAllByMealId(mealId: Int): Stream<DbCuisineDto> {
        return databaseContext.inTransaction { handle ->
            handle.attach(CuisineDao::class.java).getByMealId(mealId)
        }
    }

    fun getAllByRestaurantId(restaurantId: Int): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(CuisineDao::class.java).getAllByRestaurantId(restaurantId)
        }
    }
}