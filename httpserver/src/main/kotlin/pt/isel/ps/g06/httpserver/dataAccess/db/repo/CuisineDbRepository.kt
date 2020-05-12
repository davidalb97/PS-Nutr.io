package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.CuisineDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val cuisineDaoClass = CuisineDao::class.java

@Repository
class CuisineDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getByName(name: String): Collection<CuisineDto> {
        return jdbi.inTransaction<Collection<CuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).getByName(name)
        }
    }

    fun insert(name: String): CuisineDto {
        return jdbi.inTransaction<CuisineDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).insert(name)
        }
    }

    fun getByApiIds(foodApiType: FoodApiType, cuisineIds: Collection<String>): Collection<CuisineDto> {
        return jdbi.inTransaction<Collection<CuisineDto>, Exception>(isolationLevel) {
            val apiSubmitterId = it.attach(SubmitterDao::class.java)
                    .getAllByType(SubmitterType.API.toString())
                    .first { it.submitter_name == foodApiType.toString() }
                    .submitter_id
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisineIds)
        }
    }

    fun getAllByNames(cuisineNames: List<String>): Collection<CuisineDto> {
        return jdbi.inTransaction<Collection<CuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByNames(cuisineNames)
        }
    }
}