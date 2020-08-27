package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict.DuplicateSubmitterException
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE

@Repository
class SubmitterDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getSubmitterByName(name: String): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java)
                    .getSubmitterByName(name)
        }
    }

    fun getApiSubmittersByName(names: Collection<String>): Collection<DbSubmitterDto> {
        return jdbi.inTransaction<Collection<DbSubmitterDto>, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java).getApiSubmittersByName(names)
        }
    }

    fun getSubmitterBySubmitterId(submitterId: Int): DbSubmitterDto {
        return jdbi.inTransaction<DbSubmitterDto, Exception> {
            return@inTransaction it.attach(SubmitterDao::class.java).getSubmitterBySubmitterId(submitterId)
        }
    }

    fun getSubmitterForSubmission(submissionId: Int): DbSubmitterDto? {
        return jdbi.inTransaction<DbSubmitterDto?, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(SubmitterDao::class.java)
                    .getSubmitterForSubmission(submissionId)
        }
    }

    fun insertSubmitter(name: String, type: String): DbSubmitterDto {
        return jdbi.inTransaction<DbSubmitterDto, Exception>(isolationLevel) { handle ->

            val submitterDao = handle.attach(SubmitterDao::class.java)
            if(submitterDao.getSubmitterByName(name) != null) {
                throw DuplicateSubmitterException()
            }
            return@inTransaction submitterDao
                    .insertSubmitter(name, type)
        }
    }
}