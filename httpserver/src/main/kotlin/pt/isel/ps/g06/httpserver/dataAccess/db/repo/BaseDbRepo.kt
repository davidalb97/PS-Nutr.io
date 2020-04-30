package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionContractDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VoteDao
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

@Repository
class BaseDbRepo(
        internal val jdbi: Jdbi,
        internal val defaultIsolation: TransactionIsolationLevel
) {


    /**
     * @throws InvalidInputException If submissionId is invalid or contract is unexpected.
     */
    internal fun requireSubmission(submissionId: Int, submissionType: SubmissionType) {
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
    internal fun requireSubmissionSubmitter(submissionId: Int, submitterId: Int) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if the submitter owns the submission
            it.attach(SubmissionSubmitterDao::class.java)
                    .getBySubmissionId(submissionId)
                    ?.let { if (it.submitter_id != submitterId) null else it }
                    ?: throw InvalidInputException(InvalidInputDomain.SUBMISSION_SUBMITTER,
                            "The specified submitter with id \"$submitterId\" " +
                                    "does not own submission with id \"$submissionId\"."
                    )
        }
    }

    /**
     * @throws InvalidInputException If submission was voted by submitter or if submission does not exist.
     */
    internal fun requireNoVote(submissionId: Int, submitterId: Int) {
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
    internal fun requireVote(submissionId: Int, submitterId: Int) {
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
    internal fun requireContract(submissionId: Int, contract: SubmissionContractType) {
        jdbi.inTransaction<Unit, InvalidInputException>(defaultIsolation) {

            // Check if submission is implementing the IS-A contract
            it.attach(SubmissionContractDao::class.java)
                    .getById(submissionId)
                    ?.let { if (it.submission_contract != contract.toString()) null else it }
                    ?: throw InvalidInputException(InvalidInputDomain.CONTRACT,
                            "The submission id \"$submissionId\" is not a \"$contract\"."
                    )
        }
    }
}