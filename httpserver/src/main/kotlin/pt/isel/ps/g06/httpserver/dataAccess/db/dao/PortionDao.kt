package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantCuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto

interface PortionDao {

    companion object {
        const val table = "Portion"
        const val id = "submission_id"
        const val restaurantMealId = "restaurant_meal_submission_id"
        const val quantity = "quantity"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbPortionDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbPortionDto?

    @SqlQuery("SELECT * FROM $table WHERE $restaurantMealId = :restaurantMealId")
    fun getAllByRestaurantMealId(@Bind restaurantMealId: Int): List<DbPortionDto>

    @SqlQuery("INSERT INTO $table($id, $restaurantMealId, $quantity)" +
            " VALUES(:submissionId, :restaurantMealId, :quantity) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind restaurantMealId: Int, @Bind quantity: Int): DbPortionDto

    @SqlQuery("UPDATE $table" +
            " SET $quantity = :quantity" +
            " WHERE $id = :submissionId RETURNING *"
    )
    fun update(submissionId: Int, quantity: Int): DbPortionDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun deleteById(@Bind submissionId: Int): DbPortionDto

    @SqlQuery("DELETE FROM $table" +
            " WHERE $restaurantMealId in (<restaurantMealIds>) RETURNING *")
    fun deleteAllByRestaurantMealIds(
            @BindList restaurantMealIds: List<Int>
    ): List<DbPortionDto>
}