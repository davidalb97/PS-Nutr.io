package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.DtoMapper

@SuppressWarnings("")
data class RecipeContainerDtoMapper(
        val offset: String?,
        val number: String?,
        val results: Array<RecipeDto>?
) : DtoMapper<List<RecipeDto>> {

    override fun mapDto(): List<RecipeDto> = results?.toList() ?: emptyList()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeContainerDtoMapper

        if (offset != other.offset) return false
        if (number != other.number) return false
        if (results != null) {
            if (other.results == null) return false
            if (!results.contentEquals(other.results)) return false
        } else if (other.results != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offset?.hashCode() ?: 0
        result = 31 * result + (number?.hashCode() ?: 0)
        result = 31 * result + (results?.contentHashCode() ?: 0)
        return result
    }
}

data class RecipeDto(
        val id: Int?,
        val image: String?,
        val imageUrls: Array<String>?,
        val readyInMinutes: Int?,
        val servings: Int?,
        val title: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeDto

        if (id != other.id) return false
        if (image != other.image) return false
        if (imageUrls != null) {
            if (other.imageUrls == null) return false
            if (!imageUrls.contentEquals(other.imageUrls)) return false
        } else if (other.imageUrls != null) return false
        if (readyInMinutes != other.readyInMinutes) return false
        if (servings != other.servings) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (imageUrls?.contentHashCode() ?: 0)
        result = 31 * result + (readyInMinutes ?: 0)
        result = 31 * result + (servings ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }

}