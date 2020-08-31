package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

data class ApiCuisinesDto(val cuisines: Array<String>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiCuisinesDto

        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        return cuisines.contentHashCode()
    }
}