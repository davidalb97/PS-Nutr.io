package pt.isel.ps.g06.httpserver.model

class Report(
        val reportIdentifier: Int,
        val submitterIdentifier: Int,
        val submissionIdentifier: Int,
        val text: String
)