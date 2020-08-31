package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto
import pt.isel.ps.g06.httpserver.model.VoteState
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

    fun getVotes(submissionId: Int): DbVotesDto {
        return databaseContext.inTransaction { handle ->
            return@inTransaction handle.attach(VotableDao::class.java).getById(submissionId)
        } ?: DbVotesDto(0, 0)
    }

    fun getUserVote(
            submissionId: Int,
            userId: Int
    ): VoteState {
        return databaseContext.inTransaction { handle ->
            val dto = handle
                    .attach(UserVoteDao::class.java)
                    .getUserVoteForSubmission(submissionId, userId)
                    ?: return@inTransaction VoteState.NOT_VOTED

            return@inTransaction if (dto.vote) VoteState.POSITIVE else VoteState.NEGATIVE
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
                    .any { it.submission_contract == contract.toString() }
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


/*

    /**
     * @throws InvalidInputException If submitter does not own the submission.
     */
    protected fun requireSubmissionSubmitter(
            submissionId: Int,
            submitterId: Int,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if the submitter owns the submission
            val submitterIds = it.attach(SubmissionSubmitterDao::class.java)
                    .getAllBySubmissionId(submissionId)
                    .map { it.submitter_id }
            if (!submitterIds.contains(submitterId)) {
                throw InvalidInputException(
                        "The specified submitter with id \"$submitterId\" " +
                                "does not own submission with id \"$submissionId\"."
                )
            }
        }
    }




    protected fun isFromApi(submissionId: Int, defaultIsolation: TransactionIsolationLevel = SERIALIZABLE): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(defaultIsolation) {
            return@inTransaction it.attach(ApiSubmissionDao::class.java)
                    .getBySubmissionId(submissionId) != null
        }
    }

    /**
     * @throws InvalidInputException If submission change timed out.
     */
    protected fun requireEditable(
            submissionId: Int,
            timeout: Duration,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        return jdbi.inTransaction<Unit, Exception>(defaultIsolation) {
            val creationDate = it.attach(SubmissionDao::class.java)
                    .getById(submissionId)!!.submission_date
            val currentTime = OffsetDateTime.now(Clock.systemDefaultZone())
            val seconds = Duration.between(creationDate, currentTime).seconds

            if (seconds > timeout.seconds) {
                throw InvalidInputException("Submission update timed out!")
            }
        }
    }

    protected fun requireFromUser(submissionId: Int, isolationLevel: TransactionIsolationLevel = SERIALIZABLE) {
        if (isFromApi(submissionId, isolationLevel)) {
            throw InvalidInputException("The submission id \"$submissionId\" is not an API submission.")
        }
    }

    protected fun getCuisinesByNames(
            cuisineNames: Collection<String>,
            isolationLevel: TransactionIsolationLevel = SERIALIZABLE
    ): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            val cuisineDtos = it.attach(CuisineDao::class.java)
                    .getAllByNames(cuisineNames)
            if (cuisineDtos.size != cuisineNames.size) {
                val invalidCuisines = cuisineNames.filter { name ->
                    cuisineDtos.none { it.cuisine_name == name }
                }
                throw InvalidInputException(
                        "Invalid cuisines: " + invalidCuisines.joinToString(",", "[", "]")
                )
            }
            return@inTransaction cuisineDtos
        }
    }

    */
}