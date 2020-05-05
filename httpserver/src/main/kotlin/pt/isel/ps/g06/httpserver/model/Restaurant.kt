package pt.isel.ps.g06.httpserver.model

class Restaurant(
        val apiId: Int,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Collection<String>
)