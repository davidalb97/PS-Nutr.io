package pt.isel.ps.g06.httpserver.dataAccess

/**
 * Responsible for mapping a dto (usually from an API or Database) to a model object
 */
interface DtoMapper<R> {
    fun mapDto(): R
}

//base -> RestaurantMapper -> 2) dbRestaurant + restaurantDto