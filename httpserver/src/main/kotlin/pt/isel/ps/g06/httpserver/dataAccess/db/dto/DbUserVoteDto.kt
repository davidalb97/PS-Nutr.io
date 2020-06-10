package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class DbUserVoteDto(
        val submission_id: Int,
        val vote_submitter_id: Int,
        val vote: Boolean
)