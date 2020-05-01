package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*

@Repository
class DbRestaurantRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val restaurantDao = RestaurantDao::class.java

    fun getRestaurantById(id: Int): RestaurantDto? {
        return inTransaction<RestaurantDto>(jdbi, serializable) {
            return@inTransaction it.attach(restaurantDao).getById(id)
        }
    }

    fun getRestaurantsByCoordinates(latitude: Float, longitude: Float, radius: Int): List<RestaurantDto>? {
        return inTransaction<List<RestaurantDto>>(jdbi, serializable) {
            it.attach(restaurantDao).getByCoordinates(latitude, longitude, radius)
        }
    }

    fun insertRestaurant(
            submitterId: Int,
            restaurantName: String,
            apiId: Int? = null,
            cuisines: List<String> = emptyList(),
            latitude: Float,
            longitude: Float,
            restaurantApiType: RestaurantApiType? = null
    ): Int? {
        return inTransaction<Int>(jdbi, serializable) {

            val submissionDao = it.attach(SubmissionDao::class.java)
            val submissionId = submissionDao.insert(SubmissionType.Restaurant.name)

            val submissionSubmitterDao = it.attach(SubmissionSubmitterDao::class.java)
            submissionSubmitterDao.insert(submissionId, submitterId)

            val restaurantDao = it.attach(SubmissionSubmitterDao::class.java)
            restaurantDao.insert(submissionId, submitterId)

            val cuisineDao = it.attach(CuisineDao::class.java)
            cuisineDao.insertAll(*cuisines.map(::CuisineParam).toTypedArray())

            if (restaurantApiType != null && apiId != null) {
                val apiDao = it.attach(ApiDao::class.java)
                val apiSubmitterId = apiDao.getIdByName(restaurantApiType.toString())
                submissionSubmitterDao.insert(submissionId, apiSubmitterId)

                val apiSubmissionDao = it.attach(ApiSubmissionDao::class.java)
                apiSubmissionDao.insert(submissionId, apiId, restaurantApiType.toString())
            }

            submissionId
        }
    }
}