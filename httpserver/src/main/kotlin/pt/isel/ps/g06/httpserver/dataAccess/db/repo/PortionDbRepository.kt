package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType.PORTION
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.util.asCachedSequence

private val portionDaoClass = PortionDao::class.java

@Repository
class PortionDbRepository(
        private val databaseContext: DatabaseContext,
        private val submissionDbRepository: SubmissionDbRepository
) {

    fun getAllByRestaurantMealId(restaurantMealId: Int): Sequence<DbPortionDto> {
        return databaseContext.inTransaction {
            it.attach(portionDaoClass)
                    .getAllForRestaurantMealId(restaurantMealId)
                    .asCachedSequence()
        }
    }

    fun getUserPortion(restaurantMealId: Int, userId: Int): DbPortionDto? {
        return databaseContext.inTransaction {
            it.attach(portionDaoClass)
                    .getByRestaurantMealIdAndUserId(restaurantMealId, userId)
        }
    }

    //TODO Should receive a restaurant Meal
    fun insert(submitterId: Int, restaurantMealId: Int, quantity: Int): DbPortionDto {
        return databaseContext.inTransaction {
            val submissionId = it.attach(SubmissionDao::class.java)
                    .insert(PORTION.toString())
                    .submission_id

            it.attach(SubmissionSubmitterDao::class.java).insert(submissionId, submitterId)
            return@inTransaction it.attach(portionDaoClass).insert(submissionId, restaurantMealId, quantity)
        }
    }

    fun update(
            submitterId: Int,
            portionIdentifier: Int,
            quantity: Int
    ): DbPortionDto {
        return databaseContext.inTransaction {

            // Check if given submission is restaurant meal
            submissionDbRepository.requireSubmissionOwner(portionIdentifier, submitterId)

            submissionDbRepository.requireSubmissionType(portionIdentifier, PORTION)

            return@inTransaction it.attach(portionDaoClass)
                    .update(portionIdentifier, quantity)
        }
    }
}