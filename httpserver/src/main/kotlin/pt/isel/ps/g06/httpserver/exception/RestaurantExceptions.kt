package pt.isel.ps.g06.httpserver.exception

class MissingRestaurantException(restaurantId: Int?) : Exception("Missing restaurant id: $restaurantId")