package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto

private const val table = "MealCuisine"
private const val mealId = "meal_submission_id"
private const val cuisineName = "cuisine_name"

interface MealCuisineDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getByMealId(@Bind mealId: Int): MealCuisineDto

    @SqlQuery("SELECT * FROM $table WHERE $cuisineName = :cuisineName")
    fun getByCuisineName(@Bind cuisineName: String): MealCuisineDto

    @SqlQuery("INSERT INTO $table($mealId, $cuisineName) VALUES(:restaurantId, :cuisineName)")
    fun insert(@Bind restaurantId: Int, @Bind cuisineName: String)
}