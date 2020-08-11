package pt.isel.ps.g06.httpserver.model

class RequestError(
        val statusCode: Int,
        val message: String
)