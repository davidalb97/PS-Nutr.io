package pt.ipl.isel.leic.ps.androidclient.data.api.dto

class ApiMealDto(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val glucoseAmount: Int,
    val carbsAmount: Int,
    val ingredients: List<String>
)