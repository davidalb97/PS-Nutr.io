package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantMealPortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.PortionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val portionDaoClass = PortionDao::class.java

class PortionDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getPortionsFromMealId(mealSubmissionId: Int): List<PortionDto> {
        return jdbi.inTransaction<List<PortionDto>, Exception>(isolationLevel) {
            return@inTransaction it.attach(portionDaoClass).getById(mealSubmissionId)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantId: Int,
            mealId: Int,
            quantity: Int
    ): RestaurantMealPortionDto {
        return jdbi.inTransaction<RestaurantMealPortionDto, Exception>(isolationLevel) {

            // Check if the mealId is from a Meal
            requireSubmission(mealId, MEAL, isolationLevel)

            // Check if the restaurantId is from a Restaurant
            requireSubmission(restaurantId, RESTAURANT, isolationLevel)

            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(PORTION.name)
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)

            it.attach(portionDaoClass).insert(submissionId, quantity)

            return@inTransaction it.attach(RestaurantMealPortionDao::class.java)
                    .insert(mealId, submissionId, restaurantId)
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

            it.attach(portionDaoClass).update(submissionId, quantity)
        }
    }
}