package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.util.asClosableSequence
import java.time.OffsetDateTime


@Repository
class SubmissionDbRepository(private val databaseContext: DatabaseContext) {

    fun getCreationDate(submissionId: Int): OffsetDateTime? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(SubmissionDao::class.java).getById(submissionId)
                    ?.submission_date
        }
    }

    fun getSubmitterForSubmissionId(submissionId: Int): DbSubmitterDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle
                    .attach(SubmitterDao::class.java)
                    .getSubmitterForSubmission(submissionId)
        }
    }

    fun getApiSubmissionById(submissionId: Int): DbApiSubmissionDto? {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(ApiSubmissionDao::class.java).getBySubmissionId(submissionId)
        }
    }

    fun getSubmissionById(submissionId: Int): DbSubmissionDto? {
        return databaseContext.inTransaction {
            it.attach(SubmissionDao::class.java).getById(submissionId)
        }
    }

    /**
     * Deletes given Submission from the database.
     * This deletion is cascading, so **any other tuples depending on given submission will be affected!**
     */
    fun deleteSubmissionById(submissionId: Int): DbSubmissionDto {
        return databaseContext.inTransaction {
            it.attach(SubmissionDao::class.java).delete(submissionId)
        }
    }

    fun hasContract(
            submissionId: Int,
            contract: SubmissionContractType
    ): Boolean {
        //TODO Better the SQL query to only get the needed contract and not all
        return databaseContext.inTransaction { handle ->
            // Check if submission is implementing the IS-A contract
            return@inTransaction handle.attach(SubmissionContractDao::class.java)
                    .getAllById(submissionId)
                    .asClosableSequence()
                    .use {
                        it.any { it.submission_contract == contract.toString() }
                    }
        }
    }

    fun isOfSubmissionType(
            submissionId: Int,
            submissionType: SubmissionType
    ): Boolean {
        return databaseContext.inTransaction {
            it
                    .attach(SubmissionDao::class.java)
                    .getById(submissionId)
                    ?.submission_type.equals(submissionType.toString())
        }
    }

    fun requireSubmissionType(
            submissionId: Int,
            submissionType: SubmissionType
    ) {
        if (!isOfSubmissionType(submissionId, submissionType)) {
            throw InvalidInputException(
                    "The specified submission with id \"$submissionId\" " +
                            "is not of type \"$submissionType\"."
            )
        }
    }

    fun isSubmissionOwner(submissionId: Int, submitterId: Int): Boolean {
        return databaseContext.inTransaction { handle ->
            // Check if the submitter owns the submission
            handle.attach(SubmissionSubmitterDao::class.java)
                    .getAllBySubmissionId(submissionId)
                    .asClosableSequence()
                    .use {
                        it.any { it.submitter_id == submitterId }
                    }
        }
    }

    fun requireSubmissionOwner(submissionId: Int, submitterId: Int) {
        if (!isSubmissionOwner(submissionId, submitterId)) {
            throw InvalidInputException(
                    "The specified submitter with id \"$submitterId\" " +
                            "does not own submission with id \"$submissionId\"."
            )
        }
    }
}