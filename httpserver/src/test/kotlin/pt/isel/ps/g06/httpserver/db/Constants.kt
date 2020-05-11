package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.model.TestFoodApi
import pt.isel.ps.g06.httpserver.model.TestIngredient
import pt.isel.ps.g06.httpserver.model.TestMeal

private val isolation = TransactionIsolationLevel.SERIALIZABLE

class Constants(val jdbi: Jdbi) {

    val serials = SerialMap(jdbi)

    val nextSubmissionId = serials["Submission"]["submission_id"]
    val nextSubmitterId = serials["Submitter"]["submitter_id"]

    val userDtos = jdbi.open().attach(UserDao::class.java)
            .getAll()

    val apiDtos = jdbi.open().attach(ApiDao::class.java)
            .getAll()

    val submitterDtos = jdbi.open().attach(SubmitterDao::class.java)
            .getAll()

    val mealIngredienteDtos = jdbi.open().attach(MealIngredientDao::class.java)
            .getAll()

    val submissionDtos = jdbi.open().attach(SubmissionDao::class.java)
            .getAll()

    val apiSubmitterDtos = jdbi.open().attach(SubmitterDao::class.java)
                .getAllByType(SubmitterType.API.toString())

    val cuisineDtos = jdbi.open().attach(CuisineDao::class.java)
            .getAll()

    val cuisineNames = cuisineDtos.map { it.cuisine_name }

    val ingredientDtos = jdbi.open().attach(IngredientDao::class.java)
            .getAll()

    val apiSubmissionDtos = jdbi.open().attach(ApiSubmissionDao::class.java)
            .getAll()

    val submissionSubmitterDtos = jdbi.open().attach(SubmissionSubmitterDao::class.java)
            .getAll()

    val insertedApiSubmissionDtos = jdbi.open().attach(ApiSubmissionDao::class.java)
            .getAll()

    val mealDtos = jdbi.open().attach(MealDao::class.java)
            .getAll()

    val mealCuisineDtos = jdbi.open().attach(MealCuisineDao::class.java)
            .getAll()

    val submissionContractDtos = jdbi.open().attach(SubmissionContractDao::class.java)
            .getAll()

    val firstFoodApi = submitterDtos.filter {
        it.submitter_type == SubmitterType.API.toString()
    }.first { submitter ->
        FoodApiType.values().any { it.toString() == submitter.submitter_name }
    }.let { submitterDto ->
        SubmitterDto(submitterDto.submitter_id, submitterDto.submitter_name, SubmitterType.API.toString())
    }

    val firstRestaurantApi = submitterDtos.filter {
        it.submitter_type == SubmitterType.API.toString()
    }.first { submitter ->
        RestaurantApiType.values().any { it.toString() == submitter.submitter_name }
    }.let { submitterDto ->
        SubmitterDto(submitterDto.submitter_id, submitterDto.submitter_name, SubmitterType.API.toString())
    }

    val ingredients: List<TestIngredient> = jdbi.open().let {
        val I_table = IngredientDao.table
        val I_id = IngredientDao.id
        val I_name = IngredientDao.name
        val AS_table = ApiSubmissionDao.table
        val AS_submissionId = ApiSubmissionDao.submissionId
        val AS_apiId = ApiSubmissionDao.apiId
        val S_table = SubmitterDao.table
        val S_id = SubmitterDao.id
        val S_name = SubmitterDao.name
        val SS_table = SubmissionSubmitterDao.table
        val SS_submissionId = SubmissionSubmitterDao.submissionId
        val SS_submitterId = SubmissionSubmitterDao.submitterId
        it.createQuery(
                "SELECT $I_table.$I_id, $I_table.$I_name, $AS_table.$AS_apiId, $S_table.$S_name, $SS_table.$SS_submitterId" +
                        " FROM $I_table" +
                        " INNER JOIN $AS_table" +
                        " ON $I_table.$I_id = $AS_table.$AS_submissionId" +
                        " INNER JOIN $SS_table" +
                        " ON $I_table.$I_id = $SS_table.$SS_submissionId" +
                        " INNER JOIN $S_table" +
                        " ON $SS_table.$SS_submitterId = $S_table.$S_id"
        ).map { rs, _ ->
            TestIngredient(rs.getString("ingredient_name"),
                    rs.getInt("submission_id"),
                    rs.getString("apiId"),
                    TestFoodApi(
                            FoodApiType.valueOf(rs.getString("submitter_name")),
                            rs.getInt("submitter_id")
                    )
            )
        }.list()
    }

    val meals: List<TestMeal> = jdbi.open().let { it ->
        val mealTable = MealDao.table
        val mealId = MealDao.id
        val mealName = MealDao.name
        val AS_table = ApiSubmissionDao.table
        val AS_submissionId = ApiSubmissionDao.submissionId
        val AS_apiId = ApiSubmissionDao.apiId
        val ST_table = SubmitterDao.table
        val ST_id = SubmitterDao.id
        val ST_name = SubmitterDao.name
        val ST_type = SubmitterDao.type
        val SN_table = SubmissionDao.table
        val SN_id = SubmissionDao.id
        val SN_type = SubmissionDao.type
        val SS_table = SubmissionSubmitterDao.table
        val SS_submissionId = SubmissionSubmitterDao.submissionId
        val SS_submitterId = SubmissionSubmitterDao.submitterId
        it.createQuery(
                "SELECT $mealTable.$mealId, $mealTable.$mealName, $AS_table.$AS_apiId, $ST_table.$ST_name, $SS_table.$SS_submitterId" +
                        " FROM Meal" +
                        " FULL OUTER JOIN $AS_table" +
                        " ON $mealTable.$mealId = $AS_table.$AS_submissionId" +
                        " INNER JOIN $SS_table" +
                        " ON $mealTable.$mealId = $SS_table.$SS_submissionId" +
                        " INNER JOIN $ST_table" +
                        " ON $SS_table.$SS_submitterId = $ST_table.$ST_id" +
                        " INNER JOIN $SN_table" +
                        " ON $SS_table.$SS_submissionId = $SN_table.$SN_id" +
                        " WHERE $ST_table.$ST_type = '${SubmitterType.User}'" +
                        " AND $SN_table.$SN_type = '${SubmissionType.MEAL}'"
        ).map { rs, _ ->
            val submissionId = rs.getInt(mealId)
            val ingredientIds = mealIngredienteDtos
                    .filter { it.meal_submission_id == submissionId }
                    .map { it.ingredient_submission_id }
            val ingredients = ingredients
                    .filter { ingredientIds.contains(it.submissionId) }
            val apiSubmitterId = getTestFoodApiByMealId(it, submissionId)!!
            val cuisines = mealCuisineDtos.filter { it.meal_submission_id == submissionId }
                    .map { it.cuisine_name }
            TestMeal(
                    rs.getString(mealName),
                    rs.getInt(SS_submitterId),
                    submissionId,
                    rs.getString(AS_apiId),
                    apiSubmitterId,
                    ingredients,
                    cuisines
            )
        }.list()
    }

    fun resetSerials() {
        serials.entries.forEach { tableColumn ->
            tableColumn.value.entries.forEach { columnValue ->
                setSerialValue(jdbi, tableColumn.key, columnValue.key, columnValue.value - 1)
            }
        }
    }
}

