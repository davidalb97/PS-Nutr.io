package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.MealDto

interface MealDao {

    companion object {
        const val table = "Meal"
        const val name = "meal_name"
        const val id = "submission_id"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<MealDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): MealDto?

    @SqlQuery("SELECT * FROM $table WHERE $name = :mealName")
    fun getByName(@Bind mealName: String): List<MealDto>

    @SqlQuery("INSERT INTO $table($id, $name) VALUES(:submissionId, :mealName) RETURNING *")
    fun insert(@Bind submissionId: Int, @Bind mealName: String): MealDto

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun delete(@Bind submissionId: Int): MealDto

    @SqlQuery("UPDATE $table SET $name = :new_name WHERE $id = :submissionId RETURNING *")
    fun update(@Bind submissionId: Int, new_name: String): MealDto
}