package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.core.transaction.TransactionIsolationLevel.SERIALIZABLE
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbCuisineDto
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import java.time.Clock
import java.time.Duration
import java.time.OffsetDateTime

@Repository

class BaseDbRepo constructor(internal val jdbi: Jdbi) {

    /**
     * @throws InvalidInputException If submissionId is invalid or contract is unexpected.
     */
    protected fun requireSubmission(
            submissionId: Int,
            submissionType: SubmissionType,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {
            // Check if the submission exists
            it.attach(SubmissionDao::class.java)
                    .getById(submissionId)
                    ?.let { if (it.submission_type != submissionType.toString()) null else it }
                    ?: throw InvalidInputException(InvalidInputDomain.SUBMISSION,
                            "Invalid $submissionType submission id \"$submissionId\"."
                    )
        }
    }

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
                throw InvalidInputException(InvalidInputDomain.SUBMISSION_SUBMITTER,
                        "The specified submitter with id \"$submitterId\" " +
                                "does not own submission with id \"$submissionId\"."
                )
            }
        }
    }

    /**
     * @throws InvalidInputException If submission was voted by submitter or if submission does not exist.
     */
    protected fun requireNoVote(
            submissionId: Int,
            submitterId: Int,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if this submitter already voted this submission
            val hasVote = it.attach(VoteDao::class.java)
                    .getUserVoteById(submissionId, submitterId)
                    .let { it != null }
            if (hasVote)
                throw InvalidInputException(InvalidInputDomain.VOTE,
                        "The submitter id \"$submitterId\" already voted on" +
                                " submission id \"$submissionId\"."
                )
        }
    }

    /**
     * @throws InvalidInputException If submission was not voted by submitter or if submission does not exist.
     */
    protected fun requireVote(
            submissionId: Int,
            submitterId: Int,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if this submitter already voted this submission
            it.attach(VoteDao::class.java)
                    .getUserVoteById(submissionId, submitterId)
                    ?: throw InvalidInputException(InvalidInputDomain.VOTE,
                            "The submitter id \"$submitterId\" not not vote on" +
                                    " submission id \"$submissionId\"."
                    )
        }
    }

    /**
     * @throws InvalidInputException If the submission does not meet the IS-A contract or
     * if the submission does not exist.
     */
    protected fun requireContract(
            submissionId: Int,
            contract: SubmissionContractType,
            defaultIsolation: TransactionIsolationLevel = SERIALIZABLE
    ) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if submission is implementing the IS-A contract
            it.attach(SubmissionContractDao::class.java)
                    .getAllById(submissionId)
                    .let { if (it.none { it.submission_contract == contract.toString() }) null else it }
                    ?: throw InvalidInputException(InvalidInputDomain.CONTRACT,
                            "The submission id \"$submissionId\" is not a \"$contract\"."
                    )
        }
    }

    protected fun isFromApi(submissionId: Int, defaultIsolation: TransactionIsolationLevel = SERIALIZABLE): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(defaultIsolation) {
            return@inTransaction it.attach(ApiSubmissionDao::class.java)
                    .getBySubmissionId(submissionId) != null
        }
    }

    protected fun isApi(submitterId: Int, defaultIsolation: TransactionIsolationLevel = SERIALIZABLE): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(defaultIsolation) {
            return@inTransaction it.attach(ApiDao::class.java)
                    .getById(submitterId) != null
        }
    }

    /**
     * @throws InvalidInputException If submission change timed out.
     */
    protected fun requireEditable(submissionId: Int, timeout: Duration, defaultIsolation: TransactionIsolationLevel = SERIALIZABLE) {
        return jdbi.inTransaction<Unit, Exception>(defaultIsolation) {
            val creationDate = it.attach(SubmissionDao::class.java)
                    .getById(submissionId)!!.submission_date
            val currentTime = OffsetDateTime.now(Clock.systemDefaultZone())
            val seconds = Duration.between(creationDate, currentTime).seconds

            if(seconds > timeout.seconds) {
                throw InvalidInputException(InvalidInputDomain.TIMEOUT,
                        "Submission update timed out!"
                )
            }
        }
    }

    protected fun requireFromUser(submissionId: Int, isolationLevel: TransactionIsolationLevel) {
        if(isFromApi(submissionId, isolationLevel)) {
            throw InvalidInputException(InvalidInputDomain.API,
                    "The submission id \"$submissionId\" is not an API submission."
            )
        }
    }

    protected fun getCuisinesByNames(cuisineNames: Collection<String>, isolationLevel: TransactionIsolationLevel): Collection<DbCuisineDto> {
        return jdbi.inTransaction<Collection<DbCuisineDto>, Exception>(isolationLevel) {
            val cuisineDtos = it.attach(CuisineDao::class.java)
                    .getAllByNames(cuisineNames)
            if(cuisineDtos.size != cuisineNames.size) {
                val invalidCuisines = cuisineNames.filter { name ->
                    cuisineDtos.none { it.cuisine_name == name }
                }
                throw InvalidInputException(InvalidInputDomain.CUISINE,
                        "Invalid cuisines: " + invalidCuisines.joinToString(",", "[", "]")
                )
            }
            return@inTransaction cuisineDtos
        }
    }
}