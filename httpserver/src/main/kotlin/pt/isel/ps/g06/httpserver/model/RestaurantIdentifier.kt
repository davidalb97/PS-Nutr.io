package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.common.ID_SEPARATOR

data class RestaurantIdentifier(
        val submitterId: Int,
        val submissionId: Int? = null,
        val apiId: String? = null
) {
    override fun toString(): String {
        return StringBuilder()
                .append(submitterId)
                .append(ID_SEPARATOR)
                .append(submissionId ?: "")
                .append(ID_SEPARATOR)
                .append(apiId ?: "")
                .toString()
    }

    fun isPresentInDatabase(): Boolean {
        return submissionId != null
    }
}