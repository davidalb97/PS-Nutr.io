package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

interface ApiDtoMapper<T> {
    fun mapDto(): T
}
