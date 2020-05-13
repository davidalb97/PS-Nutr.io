package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

interface RestaurantCuisineDao {

    companion object {
        const val table = "RestaurantCuisine"
        const val id = "restaurant_submission_id"
        const val cuisineId = "cuisine_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<RestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :restaurantId")
    fun getByRestaurantId(@Bind restaurantId: Int): List<RestaurantCuisineDto>

    @SqlQuery("INSERT INTO $table($id, $cuisineId)" +
            " VALUES(:restaurantId, :cuisineId) RETURNING *")
    fun insert(@Bind restaurantId: Int, @Bind cuisineId: Int): RestaurantCuisineDto

    @SqlQuery("INSERT INTO $table($id, $cuisineId)" +
            " values <restaurantCuisineDtos> RETURNING *"
    )
    fun insertAll(@BindBeanList(propertyNames = [id, cuisineId])
                  restaurantCuisineDtos: List<RestaurantCuisineDto>
    ): List<RestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): List<RestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $id = :submission_id" +
            " AND $cuisineId in <cuisineIds> RETURNING *")
    fun deleteAllByRestaurantIdAndCuisineIds(
            @Bind restaurantSubmissionId: Int,
            @BindList cuisineIds: List<Int>
    ): List<RestaurantCuisineDto>
}
