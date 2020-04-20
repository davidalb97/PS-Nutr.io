package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery

private const val votableTable = "Votable"
private const val submissionId = "submission_id"
private const val voterSubmitterId = "vote_submitter_id"
private const val vote = "vote"

interface VoteDao {

    @SqlQuery("INSERT INTO $votableTable($submissionId, $voterSubmitterId, $vote) " +
            "VALUES(:voteSubmissionId, :voteSubmitterId, :vote)")
    fun insert(@Bind voteSubmissionId: Int, voterSubmitterId: Int, vote: Boolean): Boolean

    @SqlQuery("DELETE FROM $votableTable WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId")
    fun delete(@Bind submissionId: Int, voteSubmitterId: Int): Boolean

    @SqlQuery("UPDATE SET $votableTable SET $vote = :vote WHERE $submissionId =" +
            " :submissionId, $voterSubmitterId = :voteSubmitterId")
    fun update(@Bind submissionId: Int, voteSubmitterId: Int, vote: Boolean): Boolean
}