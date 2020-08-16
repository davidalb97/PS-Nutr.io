package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import java.util.stream.Stream

@Repository
class CuisineDbRepository(private val databaseContext: DatabaseContext) {
    fun getAll(skip: Int?, limit: Int?): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            it.attach(CuisineDao::class.java).getAll(skip ?: 0, limit)
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Collection<String>): Stream<DbCuisineDto> {
        if (cuisineIds.isEmpty()) Stream.empty<DbCuisineDto>()

        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(CuisineDao::class.java)
                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisineIds)
        }
    }

    fun getAllByNames(cuisineNames: Collection<String>): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it.attach(CuisineDao::class.java).getAllByNames(cuisineNames)
        }
    }

    fun getAllByMealId(mealId: Int): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            it.attach(CuisineDao::class.java).getByMealId(mealId)
        }
    }

    fun getAllByRestaurantId(restaurantId: Int): Stream<DbCuisineDto> {
        return databaseContext.inTransaction {
            it.attach(CuisineDao::class.java).getAllByRestaurantId(restaurantId)
        }
    }
}