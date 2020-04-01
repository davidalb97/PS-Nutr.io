package pt.isel.ps.g06.httpserver.dto

data class CuisinesDto(val cuisines: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CuisinesDto

        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        return cuisines.contentHashCode()
    }
}