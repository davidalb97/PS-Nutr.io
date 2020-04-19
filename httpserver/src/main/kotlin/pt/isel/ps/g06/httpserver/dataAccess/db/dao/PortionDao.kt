package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbPortion

private const val table = "Portion"
private const val id = "submission_id"
private const val quantity = "quantity"

interface PortionDao {

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getById(@Bind submissionId: Int): DbPortion

    @SqlQuery("INSERT INTO $table($id, $quantity) VALUES(:submissionId, :quantity)")
    fun insert(@Bind submissionId: Int, quantity: Int): Int
}