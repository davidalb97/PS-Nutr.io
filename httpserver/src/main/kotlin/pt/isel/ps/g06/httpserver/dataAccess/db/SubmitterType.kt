package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmitterType(private val type: String) {
    User(SubmitterType.USER_NAME),
    API(SubmitterType.API_NAME);

    companion object {
        const val USER_NAME = "User"
        const val API_NAME = "API"
    }

    override fun toString(): String = this.type
}