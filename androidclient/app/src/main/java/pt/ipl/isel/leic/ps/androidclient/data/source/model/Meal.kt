package pt.ipl.isel.leic.ps.androidclient.data.source.model

// TODO - add support for Room
data class Meal(
    val identifier: String,
    val name: String,
    val image_url: String,
    val info: MealInfo
)