package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbPortion
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.PortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.RestaurantMealPortionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao

class DbPortionRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val portionClass = PortionDao::class.java

    fun getPortionsFromMeal(mealSubmissionId: Int): List<DbPortion> {
        return jdbi.open().use { handle ->
            return handle.inTransaction<List<DbPortion>, Exception>(serializable) {
                it.attach(portionClass).getById(mealSubmissionId)
            }
        }
    }

    fun submitPortionToMeal(
            submitterId: Int,
            restaurantId: Int,
            mealId: Int,
            quantity: Int
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(serializable) {
                val submissionDao = it.attach(SubmissionDao::class.java)
                val submissionId = submissionDao.insert(SubmissionType.Portion.name)

                val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
                submissionSubmitterDao.insert(submissionId, submitterId)

                val portionDao = it.attach(PortionDao::class.java)
                val hasPortionID = portionDao.insert(submissionId, submitterId)

                if (hasPortionID) {
                    val restaurantMealPortionDao = it.attach(RestaurantMealPortionDao::class.java)
                    restaurantMealPortionDao.insert(mealId, submissionId, restaurantId)
                } else
                    false
            }
        }
    }
}