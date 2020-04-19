package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbCuisine

private const val table = "Cuisine"
private const val name = "cuisine_name"

interface CuisineDao {

    @SqlQuery("INSERT INTO $table($name) VALUES(:name)")
    @GetGeneratedKeys
    fun insert(@Bind name: String): Int

    @SqlQuery("SELECT * FROM $table WHERE $name = :name")
    fun getByName(@Bind name: String): List<DbCuisine>
}