package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

data class DbVotable(
        val submission_id: Int,
        val vote_submitter_id: Int,
        val vote: Boolean
)