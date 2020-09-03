package pt.isel.ps.g06.httpserver.dataAccess.db.dao

import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.SqlQuery
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionContractDto

interface SubmissionContractDao {

    companion object {
        const val table = "SubmissionContract"
        const val id = "submission_id"
        const val contract = "submission_contract"
    }

    @SqlQuery("SELECT * FROM $table WHERE $id = :submissionId")
    fun getAllById(submissionId: Int): ResultIterable<DbSubmissionContractDto>

    @SqlQuery("INSERT INTO $table($id, $contract) values <submissionContractParams> RETURNING *")
    fun insertAll(@BindBeanList(propertyNames = [id, contract])
                  submissionContractParams: Collection<SubmissionContractParam>
    ): Collection<DbSubmissionContractDto>
}

//Variable names must match sql columns
data class SubmissionContractParam(val submission_id: Int, val submission_contract: String)