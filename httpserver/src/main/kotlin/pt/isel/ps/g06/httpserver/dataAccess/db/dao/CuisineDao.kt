package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.CuisineDto

interface CuisineDao {

    companion object {
        const val table = "Cuisine"
        const val name = "cuisine_name"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<CuisineDto>

    @SqlQuery("SELECT * FROM $table WHERE $name = :name")
    fun getByName(@Bind name: String): List<CuisineDto>

    @SqlQuery("INSERT INTO $table($name) VALUES(:name) RETURNING *")
    fun insert(@Bind name: String): CuisineDto

    @SqlQuery("INSERT INTO $table($name) values <values> RETURNING *")
    fun insertAll(@BindBeanList(
            value = "values",
            propertyNames = [name]
    ) newName: List<CuisineParam>): List<CuisineDto>
}

//Variable names must match sql columns
data class CuisineParam(val cuisine_name: String)