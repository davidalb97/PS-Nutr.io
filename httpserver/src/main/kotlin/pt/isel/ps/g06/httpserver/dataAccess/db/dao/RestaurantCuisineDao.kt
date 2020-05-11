package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.RestaurantMealPortionDto

interface RestaurantCuisineDao {

    companion object {
        const val table = "RestaurantCuisine"
        const val id = "restaurant_submission_id"
        const val cuisineName = "cuisine_name"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<RestaurantMealPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :restaurantId")
    fun getByRestaurantId(@Bind restaurantId: Int): List<RestaurantCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $cuisineName = :cuisineName")
    fun getByCuisineName(@Bind cuisineName: String): List<RestaurantCuisineDto>

    @SqlQuery("INSERT INTO $table($id, $cuisineName)" +
            " VALUES(:restaurantId, :cuisineName) RETURNING *")
    fun insert(@Bind restaurantId: Int, @Bind cuisineName: String): RestaurantCuisineDto

    @SqlQuery("INSERT INTO $table($id, $cuisineName) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [id, cuisineName]
    ) newName: List<RestaurantCuisineParam>): List<RestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): List<RestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $id = :submission_id" +
            " AND $cuisineName in <values> RETURNING *")
    fun deleteAllByRestaurantIdAndCuisine(
            @Bind restaurantSubmissionId: Int,
            @BindList("values") newName: List<String>
    ): List<RestaurantCuisineDto>
}

//Variable names must match sql columns
data class RestaurantCuisineParam(
        val restaurant_submission_id: Int,
        val cuisine_name: String
)