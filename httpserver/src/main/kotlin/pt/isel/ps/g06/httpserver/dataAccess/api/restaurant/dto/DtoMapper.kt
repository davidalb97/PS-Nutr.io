package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto

interface DtoMapper<T> {
    fun mapDto(): T
}
