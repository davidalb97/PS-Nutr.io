package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealCuisineDto

interface MealCuisineDao {

    companion object {
        const val table = "MealCuisine"
        const val mealId = "meal_submission_id"
        const val cuisineName = "cuisine_name"
    }

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

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(@Bind submissionId: Int): List<MealCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $cuisineName in <values> RETURNING *")
    fun deleteAllByMealIdAndCuisine(
            @Bind submissionId: Int,
            @BindList("values") newName: List<String>
    ): List<MealCuisineDto>
}

//Variable names must match sql columns
data class MealCuisineParam(val meal_submission_id: Int, val cuisine_name: String)