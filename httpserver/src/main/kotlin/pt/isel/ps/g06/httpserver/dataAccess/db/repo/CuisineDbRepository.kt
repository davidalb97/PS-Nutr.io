package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto

private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(private val databaseContext: DatabaseContext) {
    fun getAll(skip: Int?, count: Int?): Collection<DbCuisineDto> {
        return databaseContext.inTransaction {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAll(skip, count)
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Sequence<String>): Sequence<DbCuisineDto> {
        val collection = lazy {
            val cuisines = cuisineIds.toList()

            if (cuisines.isEmpty()) return@lazy emptyList<DbCuisineDto>()

            databaseContext.inTransaction {
                return@inTransaction it
                        .attach(cuisineDaoClass)
                        .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisines)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByNames(cuisineNames: Sequence<String>): Sequence<DbCuisineDto> {
        val collection = lazy {
            databaseContext.inTransaction {
                return@inTransaction it.attach(cuisineDaoClass)
                        .getAllByNames(cuisineNames.toList())
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByMealId(mealId: Int): Sequence<DbCuisineDto> {
        val collection = lazy {
            databaseContext.inTransaction { handle ->
                handle.attach(CuisineDao::class.java).getByMealId(mealId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByRestaurantId(restaurantId: Int): Sequence<DbCuisineDto> {
        val collection = lazy {
            databaseContext.inTransaction {
                return@inTransaction it.attach(CuisineDao::class.java).getAllByRestaurantId(restaurantId)
            }
        }
        return Sequence { collection.value.iterator() }
    }
}