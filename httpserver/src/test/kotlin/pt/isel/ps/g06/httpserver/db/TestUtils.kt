package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ApiSubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.IngredientDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.MealIngredientDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.IngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.exceptions.TestParameterException
import pt.isel.ps.g06.httpserver.model.TestFoodApi
import pt.isel.ps.g06.httpserver.model.TestIngredient
import pt.isel.ps.g06.httpserver.model.TestMeal
import pt.isel.ps.g06.httpserver.model.toTestFoodApi

/**
 * Runs func with a jdbi transaction handle and rolls back after completion.
 */
fun <R> Jdbi.inSandbox(
        const: Constants,
        isolation: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE,
        func: (Handle) -> R
) {
    try {
        inTransaction<R, Exception>(isolation) { handle ->
            func(handle).also { handle.rollback() }
        }
    } finally {
        const.resetSerials()
    }
}

fun nextSerialValue(jdbi: Jdbi, tableName: String, columnName: String): Int {
    return jdbi.inTransactionUnchecked(TransactionIsolationLevel.SERIALIZABLE) {
        val nextVal = it.createQuery("SELECT * FROM nextval(pg_get_serial_sequence('$tableName', '$columnName'))")
                .map { rs, _ -> rs.getInt("nextval") }
                .list()
                .first()
        it.createUpdate("SELECT setval(pg_get_serial_sequence('$tableName', '$columnName'), ${nextVal - 1})")
                .execute()
        return@inTransactionUnchecked nextVal
    }
}

fun setSerialValue(jdbi: Jdbi, tableName: String, columnName: String, value: Int) {
    return jdbi.inTransactionUnchecked(TransactionIsolationLevel.SERIALIZABLE) {
        it.createUpdate("SELECT setval(pg_get_serial_sequence('$tableName', '$columnName'), $value)")
                .execute()
    }
}

fun List<Ingredient>.mapToTestIngredients(
        handle: Handle,
        api: SubmitterDto,
        ingredients: List<Ingredient>
): List<TestIngredient> {
    val testFoodApi = TestFoodApi(
            FoodApiType.valueOf(api.submitter_name),
            api.submitter_id
    )
    return handle.attach(ApiSubmissionDao::class.java)
            .getAllBySubmitterIdSubmissionTypeAndApiIds(
                    api.submitter_id,
                    SubmissionType.INGREDIENT.toString(),
                    ingredients.map { it.apiId }
            )
            .map { dto ->
                val ingredient = ingredients.first { it.apiId == dto.apiId }
                TestIngredient(
                        ingredient.name,
                        dto.submission_id,
                        dto.apiId,
                        testFoodApi
                )
            }
}

fun getDtosFromIngredientNames(
        handle: Handle,
        apiSubmitter: Int,
        ingredientNames: List<String>
): List<IngredientDto> {

    val ingredientDtos = handle.attach(IngredientDao::class.java)
            .getAllBySubmitterId(apiSubmitter)
            .filter { ingredientNames.contains(it.ingredient_name) }
    if (ingredientDtos.size != ingredientNames.size) {
        throw TestParameterException("ingredientNames size \""
                + ingredientNames.size +
                "\" is different from result ingredientDtos \"" +
                +ingredientDtos.size +
                "\" !")
    }
    return ingredientDtos
}

/*
fun findFirstMealWithIngredients(handle: Handle): TestMeal {
    val mealIngredientDtos = handle.attach(MealIngredientDao::class.java)
            .getAll()
    var mealIngredientIds: List<Int>
    val mealSubmissionId = handle.attach(SubmissionDao::class.java).getAll()
            //Only meal submissions
            .filter { it.submission_type == SubmissionType.MEAL.toString() }
            .map { it.submission_id }
            //Meal with ingredients
            .first { mealId -> mealIngredientDtos.any { it.meal_submission_id == mealId } }
    val mealDto = handle.attach(MealDao::class.java).getById(mealSubmissionId)!!
    val mealIngredients = mealIngredientDtos
            .filter { it.meal_submission_id == mealSubmissionId }
    val api = handle.attach(SubmitterDao::class.java).getBySubmissionId(
            mealIngredients.first().ingredient_submission_id
    )

}
*/

fun getTestFoodApiBySubmissionId(handle: Handle, submissionId: Int): TestFoodApi? {
    return handle.attach(SubmitterDao::class.java)
            .getAllBySubmissionId(submissionId)
            .firstOrNull { it.submitter_type == SubmitterType.API.toString() }
            ?.toTestFoodApi()
}

fun getTestFoodApiByMealId(handle: Handle, submissionId: Int): TestFoodApi? {
    return getTestFoodApiBySubmissionId(handle, submissionId)
            ?: getTestFoodApiBySubmissionId(handle,
                    handle.attach(MealIngredientDao::class.java)
                            .getAllByMealId(submissionId)
                            .first()
                            .ingredient_submission_id
            )
}

fun MealDto.mapToTest(handle: Handle): TestMeal {
    val mealIngredientIds = handle.attach(MealIngredientDao::class.java)
            .getAllByMealId(this.submission_id)
            .map { it.ingredient_submission_id }
    val mealIngredients = handle.attach(IngredientDao::class.java)
            .getAllByIds(mealIngredientIds)
    val apiSubmitter = getTestFoodApiByMealId(handle, this.submission_id)!!

    val testIngredients = handle.attach(ApiSubmissionDao::class.java)
            .getAllBySubmissionIds(mealIngredientIds)
            .map { dto ->
                val ingredient = mealIngredients
                        .first { it.submission_id == dto.submission_id }
                TestIngredient(
                        ingredient.ingredient_name,
                        dto.submission_id,
                        dto.apiId,
                        apiSubmitter
                )
            }

    val apiSubmission = handle.attach(ApiSubmissionDao::class.java)
            .getBySubmissionId(submission_id)

    return TestMeal(
            this.meal_name,
            apiSubmitter.submitterId,
            this.submission_id,
            apiSubmission?.apiId,
            apiSubmitter,
            testIngredients
    )
}