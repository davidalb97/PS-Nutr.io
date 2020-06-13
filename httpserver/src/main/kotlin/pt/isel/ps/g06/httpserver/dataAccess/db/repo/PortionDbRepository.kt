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
                handle.attach(PortionDao::class.java)
                        .getAllByRestaurantMealId(restaurantMealId)
            }
        }
        return Sequence { collection.value.iterator() }
    }

    fun getUserPortion(restaurantMealId: Int, userId: Int): DbPortionDto? {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) { handle ->
            handle.attach(PortionDao::class.java)
                    .getByRestaurantMealIdAndUserId(restaurantMealId, userId)
        }
    }

    fun getById(restaurantMealId: Int): DbPortionDto? {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) {
            return@inTransaction it.attach(portionDaoClass).getById(restaurantMealId)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantMealId: Int,
            quantity: Int
    ): DbPortionDto {
        return jdbi.inTransaction<DbPortionDto, Exception>(isolationLevel) {

            // Check if the restaurant meal
            requireSubmission(restaurantMealId, RESTAURANT_MEAL, isolationLevel)

            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(PORTION.toString())
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)

            return@inTransaction it.attach(portionDaoClass)
                    .insert(submissionId, restaurantMealId, quantity)
        }
    }

    fun update(
            submitterId: Int,
            submissionId: Int,
            quantity: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the mealId is from a Meal
            requireSubmission(submissionId, PORTION, isolationLevel)

            // Check if the submitter is the submission owner
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            it.attach(portionDaoClass).update(submissionId, quantity)
        }
    }

    fun remove(
            submitterId: Int,
            submissionId: Int,
            quantity: Int
    ) {
        return jdbi.inTransaction<Unit, Exception>(isolationLevel) {

            // Check if the mealId is from a Meal
            requireSubmission(submissionId, PORTION, isolationLevel)

            // Check if the submitter is the submission owner
            requireSubmissionSubmitter(submissionId, submitterId, isolationLevel)

            it.attach(portionDaoClass).deleteById(submissionId)
        }
    }
}