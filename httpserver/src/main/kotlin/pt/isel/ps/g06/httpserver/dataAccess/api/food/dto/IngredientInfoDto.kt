package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

//Missing fields:
//"meta": [],
//"original": null,
//"originalName": null,
data class IngredientInfoDto(
        val id: Int?,
        val aisle: String?,
        val name: String?,
        val image: String?,
        val amount: Float?,
        val consistency: String?,
        val estimatedCost: EstimatedCostDto?,
        val shoppingListUnits: Array<String>?,
        val unit: String?,
        val unitLong: String?,
        val unitShort: String?,
        val possibleUnits: Array<String>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngredientInfoDto

        if (id != other.id) return false
        if (aisle != other.aisle) return false
        if (name != other.name) return false
        if (image != other.image) return false
        if (amount != other.amount) return false
        if (consistency != other.consistency) return false
        if (estimatedCost != other.estimatedCost) return false
        if (shoppingListUnits != null) {
            if (other.shoppingListUnits == null) return false
            if (!shoppingListUnits.contentEquals(other.shoppingListUnits)) return false
        } else if (other.shoppingListUnits != null) return false
        if (unit != other.unit) return false
        if (unitLong != other.unitLong) return false
        if (unitShort != other.unitShort) return false
        if (possibleUnits != null) {
            if (other.possibleUnits == null) return false
            if (!possibleUnits.contentEquals(other.possibleUnits)) return false
        } else if (other.possibleUnits != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (aisle?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (amount?.hashCode() ?: 0)
        result = 31 * result + (consistency?.hashCode() ?: 0)
        result = 31 * result + (estimatedCost?.hashCode() ?: 0)
        result = 31 * result + (shoppingListUnits?.contentHashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + (unitLong?.hashCode() ?: 0)
        result = 31 * result + (unitShort?.hashCode() ?: 0)
        result = 31 * result + (possibleUnits?.contentHashCode() ?: 0)
        return result
    }
}

data class EstimatedCostDto(
        val unit: String,
        val value: Float
)