package pt.ipl.isel.leic.ps.androidclient.data.source.model.restaurant

// TODO
data class Restaurant(
        val apiId: Int,
        val apiType: String,
        override val name: String,
        val votes: List<Boolean>,
        val cuisines: Array<String>
) : ARestaurant(apiType, name) {

}