package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

import pt.isel.ps.g06.httpserver.dataAccess.api.IUnDto

data class DailyMenuDto(
        val daily_menu: Array<DailyMenuContainerDto>?,
        val code: Int?,
        val status: String?,
        val message: String?
) : IUnDto<List<String>> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyMenuDto

        if (daily_menu != null) {
            if (other.daily_menu == null) return false
            if (!daily_menu.contentEquals(other.daily_menu)) return false
        } else if (other.daily_menu != null) return false
        if (code != other.code) return false
        if (status != other.status) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = daily_menu?.contentHashCode() ?: 0
        result = 31 * result + (code ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun unDto(): List<String> {
        if (code != 200) return emptyList()
        return daily_menu!!
                .flatMap { container -> container.dishes!!.asList() }
                .map { it.name!! }
    }
}

data class DailyMenuContainerDto(
        val daily_menu_id: Int?,
        val name: String?,
        val start_date: String?,
        val end_date: String?,
        val dishes: Array<DishDto>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyMenuContainerDto

        if (daily_menu_id != other.daily_menu_id) return false
        if (name != other.name) return false
        if (start_date != other.start_date) return false
        if (end_date != other.end_date) return false
        if (dishes != null) {
            if (other.dishes == null) return false
            if (!dishes.contentEquals(other.dishes)) return false
        } else if (other.dishes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = daily_menu_id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (start_date?.hashCode() ?: 0)
        result = 31 * result + (end_date?.hashCode() ?: 0)
        result = 31 * result + (dishes?.contentHashCode() ?: 0)
        return result
    }
}

data class DishDto(
        val dish_id: Int?,
        val name: String?,
        val price: String?
)