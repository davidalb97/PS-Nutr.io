package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Jdbi
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmitterType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

class InsertConstants(val jdbi: Jdbi) {

    val nextSubmissionId = nextSerialValue(jdbi, "Submission", "submission_id")
    val nextSubmitterId = nextSerialValue(jdbi, "Submitter", "submitter_id")
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

    val ingredients: List<Ingredient> = jdbi.open().createQuery(
            "SELECT Ingredient.ingredient_name, ApiSubmission.apiId, Submitter.submitter_name" +
                    " FROM Ingredient" +
                    " INNER JOIN ApiSubmission" +
                    " ON Ingredient.submission_id = ApiSubmission.submission_id" +
                    " INNER JOIN SubmissionSubmitter" +
                    " ON Ingredient.submission_id = SubmissionSubmitter.submission_id" +
                    " INNER JOIN Submitter" +
                    " ON SubmissionSubmitter.submitter_id = Submitter.submitter_id"
    ).map { rs, ctx ->
        Ingredient(rs.getString("ingredient_name"),
                rs.getInt("apiId"),
                FoodApiType.valueOf(rs.getString("submitter_name"))
        )
    }.list()

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

    val firstFoodApi by lazy {
        val submitterDto = submitterDtos.filter {
            it.submitter_type == SubmitterType.API.toString()
        }.first { submitter ->
            FoodApiType.values().any { it.toString() == submitter.submitter_name }
        }
        SubmitterDto(submitterDto.submitter_id, submitterDto.submitter_name, SubmitterType.API.toString())
    }

    val firstRestaurantApi by lazy {
        val submitterDto = submitterDtos.filter {
            it.submitter_type == SubmitterType.API.toString()
        }.first { submitter ->
            RestaurantApiType.values().any { it.toString() == submitter.submitter_name }
        }
        SubmitterDto(submitterDto.submitter_id, submitterDto.submitter_name, SubmitterType.API.toString())
    }
}
