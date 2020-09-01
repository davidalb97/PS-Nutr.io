package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.PORTION
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.RESTAURANT_MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val portionDaoClass = PortionDao::class.java

@Repository
class PortionDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAllByRestaurantMealId(restaurantMealId: Int): Sequence<DbPortionDto> {
        val collection = lazy {
            jdbi.inTransaction<Collection<DbPortionDto>, Exception>(isolationLevel) { handle ->
                handle
                        .attach(PortionDao::class.java)
                        .getAllForRestaurantMealId(restaurantMealId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getUserPortion(restaurantMealId: Int, userId: Int): DbPortionDto? {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) { handle ->
            handle
                    .attach(PortionDao::class.java)
                    .getByRestaurantMealIdAndUserId(restaurantMealId, userId)
        }
    }


    fun insert(
            submitterId: Int,
            restaurantMealId: Int,
            quantity: Int
    ): DbPortionDto {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) {
            // Check if given submission is restaurant meal
            requireSubmission(restaurantMealId, RESTAURANT_MEAL, isolationLevel)

            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(PORTION.toString())
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)

            return@inTransaction it.attach(portionDaoClass).insert(submissionId, restaurantMealId, quantity)
        }
    }

    fun update(
            portionIdentifier: Int,
            quantity: Int
    ): DbPortionDto {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) {
            // Check if given submission is restaurant meal
            requireSubmission(portionIdentifier, PORTION, isolationLevel)

            return@inTransaction it.attach(portionDaoClass).update(portionIdentifier, quantity)
        }
    }
}