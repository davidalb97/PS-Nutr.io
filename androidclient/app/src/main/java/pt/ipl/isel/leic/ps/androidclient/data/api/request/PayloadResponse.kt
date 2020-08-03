package pt.ipl.isel.leic.ps.androidclient.data.api.request

data class PayloadResponse(
    val headers: Map<String, String>,
    val status: Int,
    val body: String
)