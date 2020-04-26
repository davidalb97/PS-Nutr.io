package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.VotableDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

private const val votableTable = "Votable"
private const val submissionId = "submission_id"
private const val voterSubmitterId = "vote_submitter_id"
private const val vote = "vote"

interface VotableDao {

    @SqlQuery("SELECT ? FROM $votableTable WHERE $submissionId = :submissionId")
    fun getVotes(@Bind submissionId: Int): Votes

    @SqlQuery("INSERT INTO $votableTable($submissionId, $voterSubmitterId, $vote) " +
            "VALUES(:voteSubmissionId, :voteSubmitterId, :vote) RETURNING *")
    fun insert(@Bind voteSubmissionId: Int, voterSubmitterId: Int, vote: Boolean): VotableDto

    @SqlUpdate("UPDATE SET $votableTable SET $vote = :vote WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId")
    fun update(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean)

    @SqlUpdate("DELETE FROM $votableTable WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId")
    fun delete(@Bind submissionId: Int, voteSubmitterId: Int)
}