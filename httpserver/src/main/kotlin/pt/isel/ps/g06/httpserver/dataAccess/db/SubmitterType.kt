package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmitterType(private val type: String) {
    User("User"),
    API("API");

    override fun toString(): String = this.type
}