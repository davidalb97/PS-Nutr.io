package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmitterDbRepository(jdbi: Jdbi): BaseDbRepo(jdbi) {

    fun getBySubmissionId(submissionId: Int): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto?, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(SubmitterDao::class.java)
                    .getAllBySubmissionId(submissionId).firstOrNull() ?: return@inTransaction null
        }
    }
}