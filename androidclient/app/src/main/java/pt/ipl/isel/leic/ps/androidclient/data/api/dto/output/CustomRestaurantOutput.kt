package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

class CustomRestaurantOutput(
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val cuisines: Collection<String>
)