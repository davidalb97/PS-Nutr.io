package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto

interface VotableDao {

    companion object {
        const val table = "Votes"
        const val id = "submission_id"
        const val positiveCount = "positive_count"
        const val negativeCount = "negative_count"
    }

    @SqlQuery("SELECT $positiveCount, $negativeCount" +
            " FROM $table" +
            " WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbVotesDto?

    @SqlQuery("INSERT INTO $table($id, $positiveCount, $negativeCount)" +
            " VALUES(:submissionId, :positiveOffset, :negativeOffset) " +
            " ON CONFLICT($id) DO" +
            " UPDATE SET" +
            " $positiveCount = $table.$positiveCount + :positiveOffset," +
            " $negativeCount = $table.$negativeCount + :negativeOffset" +
            " RETURNING *"
    )
    fun incrementVotes(@Bind submissionId: Int, @Bind positiveOffset: Int, @Bind negativeOffset: Int): DbVotesDto
}