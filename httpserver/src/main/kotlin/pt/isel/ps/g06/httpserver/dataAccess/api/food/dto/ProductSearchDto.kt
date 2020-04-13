package pt.isel.ps.g06.httpserver.dataAccess.api.food.dto

data class ProductSearchContainerDto(
        val products: Array<ProductDto>?,
        val totalProducts: Int?,
        val type: String?,
        val offset: Int?,
        val number: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductSearchContainerDto

        if (products != null) {
            if (other.products == null) return false
            if (!products.contentEquals(other.products)) return false
        } else if (other.products != null) return false
        if (totalProducts != other.totalProducts) return false
        if (type != other.type) return false
        if (offset != other.offset) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        var result = products?.contentHashCode() ?: 0
        result = 31 * result + (totalProducts ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (offset ?: 0)
        result = 31 * result + (number ?: 0)
        return result
    }
}

data class ProductDto(
        val id: Int?,
        val title: String?,
        val image: String?,
        val imageType: String?
)