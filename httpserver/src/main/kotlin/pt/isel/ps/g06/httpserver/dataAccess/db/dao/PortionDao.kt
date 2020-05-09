package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.PortionDto

interface PortionDao {

    companion object {
        const val table = "Portion"
        const val id = "submission_id"
        const val quantity = "quantity"
    }

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): List<PortionDto>

    @SqlQuery("INSERT INTO $table($id, $quantity)" +
            " VALUES(:submissionId, :quantity) RETURNING *")
    fun insert(@Bind submissionId: Int, quantity: Int): PortionDto

    @SqlQuery("UPDATE $table" +
            " SET $quantity = :quantity" +
            " WHERE $id = :submissionId RETURNING *"
    )
    fun update(submissionId: Int, quantity: Int): PortionDto
}