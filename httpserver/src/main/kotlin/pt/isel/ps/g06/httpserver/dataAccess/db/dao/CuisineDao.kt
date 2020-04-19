package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbCuisine

private const val cuisineTable = "Cuisine"
private const val cuisineName = "cuisine_name"

interface CuisineDao {

    @SqlQuery("INSERT INTO $cuisineTable($cuisineName) VALUES(:name)")
    @GetGeneratedKeys
    fun insertCuisine(@Bind name: String): Int

    @SqlQuery("SELECT * FROM $cuisineTable WHERE $cuisineName = :name")
    fun getCuisinesByName(@Bind name: String): List<DbCuisine>
}