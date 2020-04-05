package pt.isel.ps.g06.httpserver.model

abstract class BaseRestaurant {
    abstract fun getRestaurantId():Int
    abstract fun getRestaurantName() : String
    abstract fun getRestaurantLatitude():Float
    abstract fun getRestaurantLongitude():Float
}