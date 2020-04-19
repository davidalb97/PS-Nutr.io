package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

import pt.isel.ps.g06.httpserver.dataAccess.DtoMapper

data class ProductSearchAutoComplContainerDtoMapper(
        val results: Array<ProductSearchAutoComplDto>?
) : DtoMapper<List<ProductSearchAutoComplDto>> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductSearchAutoComplContainerDtoMapper

        if (results != null) {
            if (other.results == null) return false
            if (!results.contentEquals(other.results)) return false
        } else if (other.results != null) return false

        return true
    }

    override fun hashCode(): Int {
        return results?.contentHashCode() ?: 0
    }

    override fun mapDto(): List<ProductSearchAutoComplDto> =
            results?.toList() ?: emptyList()
}

data class ProductSearchAutoComplDto(
        val id: Int?,
        val title: String?
)