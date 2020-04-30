package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionContractDto

private const val table = "SubmissionContract"
private const val submissionId = "submission_id"
private const val contract = "submission_contract"

interface SubmissionContractDao {

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionContractDto>

    @SqlQuery("SELECT * FROM $table WHERE $submissionId = :submissionId")
    fun getById(submissionId: Int): SubmissionContractDto?

    @SqlQuery("INSERT INTO $table($submissionId, $contract) " +
            "VALUES(:submissionId, :contract) RETURNING *")
    fun insert(@Bind submissionId: Int, contract: String): SubmissionContractDto
}