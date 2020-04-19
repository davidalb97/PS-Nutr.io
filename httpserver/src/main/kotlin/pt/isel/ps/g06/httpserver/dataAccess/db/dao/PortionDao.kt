package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.concrete.DbPortion

private const val portionTable = "Portion"
private const val submissionId = "submission_id"

interface PortionDao {

    @SqlQuery("SELECT * FROM $portionTable WHERE $submissionId = :submissionId")
    fun getPortionFromSubmission(@Bind submissionId: Int): DbPortion

    @SqlQuery("INSERT INTO $portionTable($submissionId, quantity) VALUES(:submissionId, :quantity)")
    fun insertPortion(@Bind submissionId: Int, quantity: Int): Int
}