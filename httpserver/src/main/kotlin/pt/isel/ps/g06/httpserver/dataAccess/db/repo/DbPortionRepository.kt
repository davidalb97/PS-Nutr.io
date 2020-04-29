package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.PortionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantMealPortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

class DbPortionRepository(private val jdbi: Jdbi) {

    private val serializable = TransactionIsolationLevel.SERIALIZABLE
    private val portionClass = PortionDao::class.java

    fun getPortionsFromMealId(mealSubmissionId: Int): List<PortionDto>? {
        return inTransaction(jdbi, serializable) {
            it.attach(portionClass).getById(mealSubmissionId)
        }
    }

    fun insert(
            submitterId: Int,
            restaurantId: Int,
            mealId: Int,
            quantity: Int
    ): RestaurantMealPortionDto {
        return inTransaction(jdbi, serializable) {
            val submissionDao = it.attach(SubmissionDao::class.java)
            val submissionId = submissionDao
                    .insert(SubmissionType.Portion.name)
                    .submission_id

            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(submissionId, submitterId)

            val portionDao = it.attach(PortionDao::class.java)
            portionDao.insert(submissionId, quantity)

            val restaurantMealPortionDao = it.attach(RestaurantMealPortionDao::class.java)
            return@inTransaction restaurantMealPortionDao
                    .insert(mealId, submissionId, restaurantId)
        }
    }
}