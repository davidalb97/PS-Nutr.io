package pt.ipl.isel.leic.ps.androidclient.data.api.dto

data class InputRestaurantDto (
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val votes: InputVotesDto,
    val cuisines: List<String>,
    val inputMealDtos: Iterable<InputMealDto>
)