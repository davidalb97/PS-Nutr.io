package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmissionContractType(private val type: String) {
    VOTABLE("Votable"),
    REPORTABLE("Reportable"),
    API("API"),
    FAVORABLE("Favorable");

    override fun toString(): String = this.type
}