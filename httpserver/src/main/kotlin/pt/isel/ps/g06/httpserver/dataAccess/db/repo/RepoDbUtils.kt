package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.util.log

fun <R> inTransaction(jdbi: Jdbi, tl: TransactionIsolationLevel, func: (Handle) -> R): R {
    try {
        jdbi.open().use { handle ->
            return handle.inTransaction<R, Exception>(tl, func)
        }
    } catch (e: Exception) {
        log(e)
        throw e
    }
}

/**
 * @throws InvalidInputException If submitterId is invalid.
 */
fun validateSubmitterId(handle: Handle, submitterId: Int) {
    //Validate submitter id
    handle.attach(SubmissionSubmitterDao::class.java).getBySubmitterId(submitterId)
            ?: throw InvalidInputException(InvalidInputDomain.SUBMITTER,
                    "The specifed submitter id \"$submitterId\" is invalid."
            )
}

/**
 * @throws InvalidInputException If submissionId is invalid.
 */
fun validateSubmissionId(handle: Handle, submissionId: Int) {

    // Check if the submission exists
    handle.attach(SubmissionDao::class.java)
            .getById(submissionId)
            ?: throw InvalidInputException(InvalidInputDomain.SUBMISSION,
                    "Invalid submission id \"$submissionId\""
            )
}