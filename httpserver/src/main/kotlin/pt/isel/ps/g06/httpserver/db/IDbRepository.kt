package pt.isel.ps.g06.httpserver.db

interface IDbRepository {

    fun getCuisines(skip: Int, count: Int): Array<String>
}