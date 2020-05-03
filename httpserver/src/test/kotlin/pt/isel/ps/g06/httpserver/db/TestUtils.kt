package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.ApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.model.TestIngredient

/**
 * Runs func with a jdbi transaction handle and rolls back after completion.
 */
fun <R> Jdbi.inRollbackTransaction(isolation: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE, func: (Handle) -> R) {
    inTransaction<R, Exception>(isolation) { handle ->
        func(handle).also { handle.rollback() }
    }
}

fun nextSerialValue(jdbi: Jdbi, tableName: String, columnName: String): Int {
    return jdbi.open().inTransaction<Int, Exception>(TransactionIsolationLevel.SERIALIZABLE) {
        val nextVal = it.createQuery("SELECT * FROM nextval(pg_get_serial_sequence('$tableName', '$columnName'))")
                .map { rs, _ -> rs.getInt("nextval") }
                .list()
                .first()
        it.createUpdate("SELECT setval(pg_get_serial_sequence('$tableName', '$columnName'), ${nextVal - 1})")
                .execute()
        return@inTransaction nextVal
    }
}

fun zipToTestIngredient(
        apiSubmissionDtos: List<ApiSubmissionDto>,
        submissionSubmitterDtos: List<SubmissionSubmitterDto>,
        api: SubmitterDto,
        ingredients: List<Ingredient>
): List<TestIngredient> {
    //Only ApiSubmissions from selected food api
    return apiSubmissionDtos.filter { apiSub ->
        submissionSubmitterDtos.any { submissionSubmitter ->
            //If SubmissionSubmitter has relation to this ApiSubmission
            submissionSubmitter.submission_id == apiSub.submission_id
                    //If SubmissionSubmitter is from target api
                    && submissionSubmitter.submitter_id == api.submitter_id
        }
        //Only ApiSubmissions from selected food api ingredients
    }.filter { apiSub ->
        //Only ApiSubmissionDtos that
        ingredients.any { ingredient ->
            ingredient.apiId == apiSub.apiId
        }
    }.map { dto ->
        val ingredient = ingredients.first { it.apiId == dto.apiId }
        TestIngredient(
                ingredient.name,
                dto.submission_id,
                api.submitter_id,
                api.submitter_name,
                dto.apiId
        )
    }
}