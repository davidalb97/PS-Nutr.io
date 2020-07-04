package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmitterDbRepository(private val jdbi: Jdbi) {

    fun getSubmitterByName(name: String): DbSubmitterDto {
        return jdbi.inTransaction<DbSubmitterDto, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java).getSubmitterByName(name)
        }
    }

    fun getApiSubmittersByName(names: Collection<String>): Collection<DbSubmitterDto> {
        return jdbi.inTransaction<Collection<DbSubmitterDto>, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java).getApiSubmittersByName(names)
        }
    }

    fun getSubmitterForSubmission(submissionId: Int): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto?, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(SubmitterDao::class.java)
                    .getSubmitterForSubmission(submissionId)
        }
    }
}