package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

import pt.isel.ps.g06.httpserver.dataAccess.api.IUnDto

data class ProductSearchAutoComplContainerDto(
        val results: Array<ProductSearchAutoComplDto>?
) : IUnDto<List<ProductSearchAutoComplDto>> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductSearchAutoComplContainerDto

        if (results != null) {
            if (other.results == null) return false
            if (!results.contentEquals(other.results)) return false
        } else if (other.results != null) return false

        return true
    }

    override fun hashCode(): Int {
        return results?.contentHashCode() ?: 0
    }

    override fun unDto(): List<ProductSearchAutoComplDto> =
            results?.toList() ?: emptyList()
}

data class ProductSearchAutoComplDto(
        val id: Int?,
        val title: String?
)