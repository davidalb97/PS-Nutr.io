package pt.isel.ps.g06.httpserver.dataAccess.db

enum class SubmissionContractType(private val type: String) {

    VOTABLE(SubmissionContractType.VOTABLE_NAME),
    REPORTABLE(SubmissionContractType.REPORTABLE_NAME),
    API(SubmissionContractType.API_NAME),
    FAVORABLE(SubmissionContractType.FAVORABLE_NAME);

    override fun toString(): String = this.type

    companion object {
        const val VOTABLE_NAME = "Votable"
        const val REPORTABLE_NAME = "Reportable"
        const val API_NAME = "API"
        const val FAVORABLE_NAME = "Favorable"
    }
}