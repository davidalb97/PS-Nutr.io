package pt.isel.ps.g06.httpserver.model

class Restaurant(
        val identifier: String,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val cuisines: Lazy<Collection<String>>,
        val meals: Lazy<Collection<Meal>>
)