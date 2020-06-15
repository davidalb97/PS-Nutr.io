package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto

private const val Submitter_table = SubmitterDao.table
private const val Submitter_id = SubmitterDao.id
private const val Submitter_name = SubmitterDao.name
private const val Submitter_date = SubmitterDao.date

interface UserDao {
    companion object {
        const val table = "_User"
        const val id = "submitter_id"
        const val email = "email"
        const val sessionSecret = "session_secret"
    }

    @SqlQuery("SELECT " +
            "$Submitter_table.$Submitter_name, $table.$id, $table.$email, $table.$sessionSecret, $Submitter_table.$Submitter_date " +
            "FROM $table " +
            "INNER JOIN $Submitter_table " +
            "ON $table.$id = $Submitter_table.$Submitter_id " +
            "WHERE $table.$id = :submitterId"
    )
    fun getById(@Bind submitterId: Int): DbUserDto?
}