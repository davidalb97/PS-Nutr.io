package pt.isel.ps.g06.httpserver.dataAccess.db.dto

data class VotableDto(
        val submission_id: Int,
        val vote_submitter_id: Int,
        val vote: Boolean
)