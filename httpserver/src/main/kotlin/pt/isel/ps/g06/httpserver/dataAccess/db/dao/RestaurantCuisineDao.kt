package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto
import java.util.stream.Stream

interface RestaurantCuisineDao {

    companion object {
        const val table = "RestaurantCuisine"
        const val id = "restaurant_submission_id"
        const val cuisineId = "cuisine_submission_id"
    }

    @SqlQuery("SELECT * FROM $table WHERE $id = :restaurantId")
    fun getAllForRestaurantId(@Bind restaurantId: Int): Collection<DbRestaurantCuisineDto>

    @SqlQuery("INSERT INTO $table($id, $cuisineId) values <restaurantCuisineDtos> RETURNING *")
    fun insertAll(
            @BindBeanList(propertyNames = [id, cuisineId])
            restaurantCuisineDtos: Collection<DbRestaurantCuisineDto>
    ): Collection<DbRestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :restaurantId RETURNING *")
    fun deleteAllByRestaurantId(@Bind restaurantId: Int): Stream<DbRestaurantCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $id = :submission_id" +
            " AND $cuisineId in (<cuisineIds>) RETURNING *")
    fun deleteAllByRestaurantIdAndCuisineIds(
            @Bind restaurantSubmissionId: Int,
            @BindList cuisineIds: List<Int>
    ): Stream<DbRestaurantCuisineDto>
}
