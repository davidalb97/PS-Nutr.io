package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmissionContractDto

interface SubmissionContractDao {

    companion object {
        const val table = "SubmissionContract"
        const val id = "submission_id"
        const val contract = "submission_contract"
    }

    @SqlQuery("SELECT * FROM $table")
    fun getAll(): List<SubmissionContractDto>

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getAllById(submissionId: Int): List<SubmissionContractDto>

    @SqlQuery("INSERT INTO $table($id, $contract) " +
            "VALUES(:submissionId, :contract) RETURNING *")
    fun insert(@Bind submissionId: Int, contract: String): SubmissionContractDto

    @SqlQuery("INSERT INTO $table($id, $contract) values <submissionContractParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [id, contract])
                  submissionContractParams: List<SubmissionContractParam>
    ): List<SubmissionContractDto>

    @SqlQuery("DELETE FROM $table WHERE $id = :submissionId RETURNING *")
    fun deleteAllById(submissionId: Int): List<SubmissionContractDto>
}

//Variable names must match sql columns
data class SubmissionContractParam(val submission_id: Int, val submission_contract: String)