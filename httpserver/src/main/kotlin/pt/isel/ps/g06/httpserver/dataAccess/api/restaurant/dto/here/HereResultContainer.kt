package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here

data class HereResultContainer(val items: Collection<HereResultItem>)

data class HereResultItem(
        val title: String,
        val id: String,
        val position: HereLocation,

        )


data class HereLocation(val lat: Float, val lng: Float)