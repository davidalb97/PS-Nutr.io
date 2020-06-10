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

    fun insert(name: String): DbCuisineDto {
        return jdbi.inTransaction<DbCuisineDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass).insert(name)
        }
    }

//    fun getByApiIds(foodApiType: FoodApiType, cuisineIds: Collection<String>): Collection<DbCuisineDto> {
//        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
//            val apiSubmitterId = it.attach(SubmitterDao::class.java)
//                    .getAllByType(SubmitterType.API.toString())
//                    .first { it.submitter_name == foodApiType.toString() }
//                    .submitter_id
//            return@inTransaction it.attach(cuisineDaoClass)
//                    .getAllByApiSubmitterAndApiIds(apiSubmitterId, cuisineIds)
//        }
//    }

    fun getAllByNames(cuisineNames: List<String>): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(cuisineDaoClass)
                    .getAllByNames(cuisineNames)
        }
    }
}