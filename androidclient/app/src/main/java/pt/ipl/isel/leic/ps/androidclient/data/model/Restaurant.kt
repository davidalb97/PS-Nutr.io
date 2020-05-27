package pt.ipl.isel.leic.ps.androidclient.data.model

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.MealDto

// TODO - add support for Room
data class Restaurant(
    val id: Int,
    val name: String,
    val latitude: Float?,
    val longitude: Float?,
    val votes: List<Boolean>?,
    val cuisines: List<String>?,
    val meals: List<MealDto>?
)