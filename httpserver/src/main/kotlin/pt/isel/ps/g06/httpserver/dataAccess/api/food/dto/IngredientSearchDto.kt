package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

data class IngredientSearchDto(
        val name: String?,
        val image: String?,
        val id: Int?,
        val aisle: String?,
        val possibleUnits: Array<String>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngredientSearchDto

        if (name != other.name) return false
        if (image != other.image) return false
        if (id != other.id) return false
        if (aisle != other.aisle) return false
        if (possibleUnits != null) {
            if (other.possibleUnits == null) return false
            if (!possibleUnits.contentEquals(other.possibleUnits)) return false
        } else if (other.possibleUnits != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (id ?: 0)
        result = 31 * result + (aisle?.hashCode() ?: 0)
        result = 31 * result + (possibleUnits?.contentHashCode() ?: 0)
        return result
    }
}