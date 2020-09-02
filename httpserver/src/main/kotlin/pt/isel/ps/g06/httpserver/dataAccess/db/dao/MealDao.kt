package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_CUSTOM
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_SUGGESTED_MEAL
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import java.util.stream.Stream

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

//Favorite constants
private const val F_table = FavoriteDao.table
private const val F_submitter_id = FavoriteDao.submitterId
private const val F_submission_id = FavoriteDao.submissionId

interface MealDao {

    companion object {
        const val table = "Meal"
        const val name = "meal_name"
        const val id = "submission_id"
        const val carbs = "carbs"
        const val quantity = "quantity"
        const val unit = "unit"
        const val meal_type = "meal_type"
        const val attributes = "$table.$id, $table.$name, $table.$carbs, $table.$quantity, $table.$unit, $table.$meal_type"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): Stream<DbMealDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbMealDto?

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $SS_table " +
            "ON $SS_table.$SS_submissionId = $table.$id " +
            "WHERE $SS_table.$SS_submitterId = :submitterId " +
            "AND $table.$meal_type = :mealType " +
            "LIMIT :count OFFSET :skip")
    fun getAllBySubmitterIdAndType(
            @Bind submitterId: Int,
            @Bind mealType: String,
            @Bind skip: Int?,
            @Bind count: Int?
    ): Stream<DbMealDto>

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $F_table " +
            "ON $F_table.$F_submission_id = $table.$id " +
            "WHERE $F_table.$F_submitter_id = :submitterId " +
            "LIMIT :count OFFSET :skip")
    fun getAllUserFavorites(
            @Bind submitterId: Int,
            @Bind count: Int?,
            @Bind skip: Int?
    ): Stream<DbMealDto>

    @SqlQuery("SELECT $attributes" +
            " FROM $table" +
            " INNER JOIN $MC_table" +
            " ON $table.$id = $MC_table.$MC_mealId" +
            " INNER JOIN $C_table" +
            " ON $MC_table.$MC_cuisineId = $C_table.$C_cuisineId" +
            " WHERE $table.$meal_type = '$MEAL_TYPE_SUGGESTED_MEAL'" +
            " AND $C_table.$C_name IN (<cuisineNames>)" +
            " ORDER BY $table.$name ASC"
    )
    fun getAllSuggestedForCuisineNames(@BindList cuisineNames: Collection<String>): Stream<DbMealDto>

    @SqlQuery("SELECT DISTINCT ON ($table.$name) $attributes" +
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
            " AND $table.$meal_type = '$MEAL_TYPE_SUGGESTED_MEAL'" +
            " ORDER BY $table.$name ASC"
    )
    fun getAllSuggestedForApiCuisines(
            @Bind submitterId: Int,
            @BindList apiIds: Collection<String>
    ): Stream<DbMealDto>

    @SqlQuery("SELECT $attributes" +
            " FROM $table" +
            " WHERE $table.$meal_type = '$MEAL_TYPE_SUGGESTED_MEAL'" +
            " ORDER BY $table.$name ASC" +
            " LIMIT :count OFFSET :skip"
    )
    fun getAllSuggestedMeals(
            @Bind skip: Int?,
            @Bind count: Int?,
            @Bind cuisines: Collection<String>?
    ): Stream<DbMealDto>

    @SqlQuery("INSERT INTO $table($id, $name, $carbs, $quantity, $unit, $meal_type) " +
            "VALUES(:submissionId, :mealName, :carbs, :quantity, :unit, :mealType) " +
            "RETURNING *"
    )
    fun insert(
            @Bind submissionId: Int,
            @Bind mealName: String,
            @Bind carbs: Int,
            @Bind quantity: Int,
            @Bind unit: String = "gr",
            @Bind mealType: String
    ): DbMealDto

    @SqlQuery("UPDATE $table SET " +
            "$name = :mealName, " +
            "$carbs = :carbs, " +
            "$quantity = :quantity, " +
            "$unit = :unit, " +
            "$meal_type = :mealType " +
            "WHERE $id = :submissionId " +
            "RETURNING *"
    )
    fun update(
            @Bind submissionId: Int,
            @Bind mealName: String,
            @Bind carbs: Int,
            @Bind quantity: Int,
            @Bind unit: String = "gr",
            @Bind mealType: String
    ): DbMealDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): DbMealDto

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "WHERE $table.$meal_type = :mealType " +
            "ORDER BY $table.$name ASC " +
            "LIMIT :count " +
            "OFFSET :skip"
    )
    fun getAllByType(@Bind mealType: String, @Bind skip: Long?, @Bind count: Long?): Stream<DbMealDto>

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "WHERE $table.$id IN (<submissionIds>) " +
            "ORDER BY $table.$name ASC "
    )
    fun getAllIngredientsByIds(@BindList submissionIds: Collection<Int>): Stream<DbMealDto>

    @SqlQuery("SELECT $attributes " +
            "FROM $table " +
            "INNER JOIN $RM_table " +
            "ON $RM_table.$RM_mealId = $table.$id " +
            "WHERE $RM_table.$RM_restaurantId = :restaurantId " +
            "AND $table.$meal_type = '$MEAL_TYPE_CUSTOM'"
    )
    fun getAllUserMealsByRestaurantId(@Bind restaurantId: Int): Stream<DbMealDto>
}