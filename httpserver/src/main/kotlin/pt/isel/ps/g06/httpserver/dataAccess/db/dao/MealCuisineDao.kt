package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto

private const val table = "MealCuisine"
private const val mealId = "meal_submission_id"
private const val cuisineName = "cuisine_name"

interface MealCuisineDao {
    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getByMealId(@Bind mealId: Int): List<MealCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $cuisineName = :cuisineName")
    fun getByCuisineName(@Bind cuisineName: String): List<MealCuisineDto>

    @SqlQuery("INSERT INTO $table($mealId, $cuisineName)" +
            " VALUES(:restaurantId, :cuisineName) RETURNING *")
    fun insert(@Bind restaurantId: Int, @Bind cuisineName: String): MealCuisineDto

    @SqlQuery("INSERT INTO $table($mealId, $cuisineName) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [mealId, cuisineName]
    ) newName: List<MealCuisineParam>): List<MealCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submission_id RETURNING *")
    fun deleteAllByMealId(@Bind submission_id: Int): List<MealCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submission_id" +
            " AND $cuisineName in <values> RETURNING *")
    fun deleteAllByMealIdAndCuisine(
            @Bind submission_id: Int,
            @BindList("values") newName: List<String>
    ): List<MealCuisineDto>
}

//Variable names must match sql columns
data class MealCuisineParam(val meal_submission_id: Int, val cuisine_name: String)