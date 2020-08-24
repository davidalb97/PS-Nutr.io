package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto

//Restaurant table constants
private const val RM_table = RestaurantMealDao.table
private const val RM_table_submissionId = RestaurantMealDao.id
private const val RM_mealId = RestaurantMealDao.mealId
private const val RM_restaurantId = RestaurantMealDao.restaurantId

//Cuisine table constants
private const val C_table = CuisineDao.table
private const val C_name = CuisineDao.name
private const val C_cuisineId = CuisineDao.id

//ApiCuisine table constants
private const val AC_table = ApiCuisineDao.table
private const val AC_submissionId = ApiCuisineDao.submissionId
private const val AC_cuisineId = ApiCuisineDao.cuisineId

//SubmissionSubmitter table constants
private const val SS_table = SubmissionSubmitterDao.table
private const val SS_submissionId = SubmissionSubmitterDao.submissionId
private const val SS_submitterId = SubmissionSubmitterDao.submitterId

//ApiSubmission constants
private const val AS_table = ApiSubmissionDao.table
private const val AS_submissionId = ApiSubmissionDao.submissionId
private const val AS_apiId = ApiSubmissionDao.apiId

//MealCuisine constants
private const val MC_table = MealCuisineDao.table
private const val MC_mealId = MealCuisineDao.mealId
private const val MC_cuisineId = MealCuisineDao.cuisineId

//Submission constants
private const val S_table = SubmissionDao.table
private const val S_submission_id = SubmissionDao.id
private const val S_submission_type = SubmissionDao.type

//Meal Ingredient constants
private const val MI_table = MealIngredientDao.table

private const val INGREDIENT_TYPE = "Ingredient"
private const val MEAL_TYPE = "Meal"

interface MealDao {

    companion object {
        const val table = "Meal"
        const val name = "meal_name"
        const val id = "submission_id"
        const val carbs = "carbs"
        const val quantity = "quantity"
        const val unit = "unit"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<DbMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbMealDto?

    @SqlQuery("SELECT * FROM $table INNER JOIN $SS_table ON " +
            "$SS_table.$SS_submissionId = $table.$id " +
            "WHERE $SS_table.$SS_submitterId = :submitterId")
    fun getAllBySubmitterId(@Bind submitterId: Int): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit " +
            "FROM $table " +
            "INNER JOIN $RM_table " +
            "ON $table.$id = $RM_table.$RM_mealId " +
            "WHERE $RM_table.$RM_restaurantId = :restaurantId"
    )
    fun getAllByRestaurantId(@Bind restaurantId: Int): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit" +
            " FROM $table" +
            " INNER JOIN $MC_table" +
            " ON $table.$id = $MC_table.$MC_mealId" +
            " INNER JOIN $C_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " LEFT JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $table.$id" +
            " WHERE $SS_table.$SS_submissionId IS NULL" +
            " AND $C_table.$C_name IN (<cuisineNames>)" +
            " ORDER BY $table.$name ASC"
    )
    fun getAllSuggestedForCuisineNames(@BindList cuisineNames: Collection<String>): Collection<DbMealDto>

    @SqlQuery("SELECT DISTINCT ON ($table.$name) $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit" +
            " FROM $C_table" +
            " INNER JOIN $AC_table" +
            " ON $C_table.$C_cuisineId = $AC_table.$AC_cuisineId" +
            " INNER JOIN $AS_table" +
            " ON $AS_table.$AS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $AC_table.$AC_submissionId" +
            " INNER JOIN $MC_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " INNER JOIN $table" +
            " ON $table.$id = $MC_table.$MC_mealId" +
            " WHERE $SS_table.$SS_submitterId = :submitterId" +
            " AND $AS_table.$AS_apiId IN (<apiIds>)" +
            " ORDER BY $table.$name ASC"
    )
    fun getAllSuggestedForApiCuisines(
            @Bind submitterId: Int,
            @BindList apiIds: Collection<String>
    ): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit" +
            " FROM $table" +
            " INNER JOIN $S_table" +
            " ON $S_table.$S_submission_id = $table.$id" +
            " LEFT JOIN $SS_table" +
            " ON $SS_table.$SS_submissionId = $table.$id" +
            " WHERE $SS_table.$SS_submissionId IS NULL" +
            " AND $S_table.$S_submission_type = '$MEAL_TYPE'" +
            " ORDER BY $table.$name ASC" +
            " LIMIT :count OFFSET :skip"
    )
    fun getAllSuggestedMeals(
            @Bind count: Int?,
            @Bind skip: Int?,
            @Bind cuisines: Collection<String>?
    ): Collection<DbMealDto>

    @SqlQuery("INSERT INTO $table($id, $name, $carbs, $quantity, $unit) " +
            "VALUES(:submissionId, :mealName, :carbs, :quantity, :unit) " +
            "RETURNING *"
    )
    fun insert(
            @Bind submissionId: Int,
            @Bind mealName: String,
            @Bind carbs: Int,
            @Bind quantity: Int,
            @Bind unit: String = "gr"
    ): DbMealDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbMealDto

    @SqlQuery("UPDATE $table SET $name = :new_name WHERE $id = :submissionId RETURNING *")
    fun updateName(@Bind submissionId: Int, new_name: String): DbMealDto

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit " +
            "FROM $table " +
            "INNER JOIN $S_table " +
            "ON $S_table.$S_submission_id = $table.$id " +
            "WHERE $S_table.$S_submission_type = '$INGREDIENT_TYPE' " +
            "ORDER BY $table.$name ASC " +
            "LIMIT :limit " +
            "OFFSET :skip"
    )
    fun getAllIngredients(skip: Int = 0, limit: Int? = 10): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit " +
            "FROM $table " +
            "INNER JOIN $S_table " +
            "ON $S_table.$S_submission_id = $table.$id " +
            "WHERE $S_table.$S_submission_type = '$INGREDIENT_TYPE' " +
            "AND $table.$id IN (<submissionIds>) " +
            "ORDER BY $table.$name ASC "
    )
    fun getAllIngredientsByIds(@BindList submissionIds: Collection<Int>): Collection<DbMealDto>

    @SqlQuery("SELECT $table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit " +
            "FROM $table " +
            "INNER JOIN $RM_table " +
            "ON $RM_table.$RM_mealId = $table.$id " +
            "INNER JOIN $SS_table " +
            "ON $RM_table.$RM_table_submissionId = $SS_table.$SS_submissionId " +
            "WHERE $RM_table.$RM_restaurantId = :restaurantId")
    fun getAllUserMealsByRestaurantId(@Bind restaurantId: Int): Collection<DbMealDto>
}