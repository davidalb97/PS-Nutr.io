package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

class RestaurantOutput(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val cuisines: Collection<String>
)