package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto


private const val table = "Cuisine"
private const val name = "cuisine_name"

interface CuisineDao {

    @SqlQuery("INSERT INTO $table($name) VALUES(:name)")
    fun insert(@Bind name: String)

    @SqlUpdate("INSERT INTO $table($name) values <values>")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [name]
    ) vararg newName: CuisineParam)

    @SqlQuery("SELECT * FROM $table WHERE $name = :name")
    fun getByName(@Bind name: String): List<CuisineDto>
}

//Variable names must match sql columns!!!
data class CuisineParam(val cuisine_name: String)