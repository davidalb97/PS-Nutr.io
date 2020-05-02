package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.IngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.UserDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.db.nextSerialValue

class InsertConstants(val jdbi: Jdbi) {

    val userSubmitterId = 1
    val apiSubmitterId = 2
    val nextSubmissionId = nextSerialValue(jdbi, "Submission", "submission_id")
    val nextSubmitterId = nextSerialValue(jdbi, "Submitter", "submitter_id")

    val insertedUserDto = jdbi.open().attach(UserDao::class.java)
            .getById(userSubmitterId)!!

    val insertedUserSubmitterDto = jdbi.open().attach(SubmissionSubmitterDao::class.java)
            .getAll()
            .first { it.submitter_id == userSubmitterId }

    val insertedApiDto =jdbi.open().attach(ApiDao::class.java)
            .getById(apiSubmitterId)!!

    val insertedApiSubmitterDto = jdbi.open().attach(SubmissionSubmitterDao::class.java)
            .getAll()
            .first { it.submitter_id == apiSubmitterId }

    val insertedSubmissions = jdbi.open().attach(SubmissionDao::class.java)
            .getAll()

    val insertedCuisineDtos = jdbi.open().attach(CuisineDao::class.java)
            .getAll()

    val insertedCuisineNames = insertedCuisineDtos.map { it.cuisine_name }

    val insertedIngredientDtos = jdbi.open().attach(IngredientDao::class.java)
            .getAll()

    val insertedIngredients = jdbi.open().createQuery(
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

    val insertedIngredientIds = insertedIngredientDtos.map { it.submission_id }

    val apiSubmissionDtos = jdbi.open().attach(ApiSubmissionDao::class.java)
            .getAll()
}