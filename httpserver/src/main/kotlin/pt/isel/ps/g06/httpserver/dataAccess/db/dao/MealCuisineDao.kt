package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealCuisineDto

//Cuisine table constants
private const val C_table = CuisineDao.table
private const val C_name = CuisineDao.name
private const val C_cuisineId = CuisineDao.id

//ApiSubmission constants
private const val M_table = MealDao.table
private const val M_name = MealDao.name
private const val M_id = MealDao.id

interface MealCuisineDao {

    companion object {
        const val table = "MealCuisine"
        const val mealId = "meal_submission_id"
        const val cuisineId = "cuisine_submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealCuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $mealId = :mealId")
    fun getAllFromMealId(@Bind mealId: Int): List<DbMealCuisineDto>

    @SqlQuery("INSERT INTO $table($mealId, $cuisineId)" +
            " VALUES(:submissionId, :cuisineName) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind cuisineId: Int): DbMealCuisineDto

    @SqlQuery("INSERT INTO $table($mealId, $cuisineId) values <mealCuisineDtos> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [mealId, cuisineId])
                  mealCuisineDtos: List<DbMealCuisineDto>): List<DbMealCuisineDto>

    @SqlQuery("DELETE FROM $table WHERE $mealId = :submissionId RETURNING *")
    fun deleteAllByMealId(@Bind submissionId: Int): List<DbMealCuisineDto>

    @SqlQuery("DELETE FROM $table" +
            " WHERE $mealId = :submissionId" +
            " AND $cuisineId in (<cuisineIds>) RETURNING *")
    fun deleteAllByMealIdAndCuisineIds(
            @Bind submissionId: Int,
            @BindList cuisineIds: Collection<Int>
    ): Collection<DbMealCuisineDto>
}