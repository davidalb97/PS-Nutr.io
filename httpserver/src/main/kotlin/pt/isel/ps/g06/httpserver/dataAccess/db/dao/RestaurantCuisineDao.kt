package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto

interface RestaurantCuisineDao {

    companion object {
        const val table = "RestaurantCuisine"
        const val id = "restaurant_submission_id"
        const val cuisineId = "cuisine_submission_id"
    }

    @SqlQuery("SELECT * FROM $table WHERE $id = :restaurantId")
    fun getAllForRestaurantId(@Bind restaurantId: Int): ResultIterable<DbRestaurantCuisineDto>

    @SqlQuery("INSERT INTO $table($id, $cuisineId) values <restaurantCuisineDtos> RETURNING *")
    fun insertAll(
            @BindBeanList(propertyNames = [id, cuisineId])
            restaurantCuisineDtos: Collection<DbRestaurantCuisineDto>
    ): Collection<DbRestaurantCuisineDto>
}
