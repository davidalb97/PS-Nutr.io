package pt.isel.ps.g06.httpserver.model

data class RestaurantIdentifier(
        val submitterId: Int,
        val submissionId: Int? = null,
        val apiId: String? = null
) {
    override fun toString(): String {
        return StringBuilder(submitterId)
                .append(submitterId)
                .append(apiId)
                .toString()
    }
}