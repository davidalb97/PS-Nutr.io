package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getByName(name: String): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).getByName(name)
        }
    }

    fun getAll(skip: Int?, limit: Int?): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it
                    .attach(cuisineDaoClass)
                    .getAll(skip ?: 0, limit)
        }
    }

    fun insert(name: String): DbCuisineDto {
        return jdbi.inTransaction<DbCuisineDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).insert(name)
        }
    }

    fun getByApiIds(apiSubmitterId: Int, cuisineIds: Sequence<String>): Sequence<DbCuisineDto> {
        val collection = lazy {
            val cuisines = cuisineIds.toList()

            if (cuisines.isEmpty()) return@lazy emptyList<DbCuisineDto>()

            jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
                return@inTransaction it
                        .attach(cuisineDaoClass)
                        .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisines)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByNames(cuisineNames: Sequence<String>): Sequence<DbCuisineDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
                return@inTransaction it.attach(cuisineDaoClass)
                        .getAllByNames(cuisineNames.toList())
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByMealId(mealId: Int): Sequence<DbCuisineDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) { handle ->
                handle.attach(CuisineDao::class.java).getByMealId(mealId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getAllByRestaurantId(restaurantId: Int): Sequence<DbCuisineDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
                return@inTransaction it.attach(CuisineDao::class.java).getAllByRestaurantId(restaurantId)
            }
        }
        return Sequence { collection.value.iterator() }
    }
}