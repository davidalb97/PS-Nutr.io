package pt.isel.ps.g06.httpserver.dataAccess.db.concrete

data class DbSubmitter(
        val submitter_id: Int,
        val submitter_name: String,
        val submitter_type: String
)