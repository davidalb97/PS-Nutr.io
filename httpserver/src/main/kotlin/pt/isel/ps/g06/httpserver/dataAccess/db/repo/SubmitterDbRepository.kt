package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateSubmitterException
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import java.util.stream.Stream

@Repository
class SubmitterDbRepository(private val databaseContext: DatabaseContext) {

    fun getSubmitterByName(name: String): DbSubmitterDto? {
        return databaseContext.inTransaction {
            it.attach(SubmitterDao::class.java).getSubmitterByName(name)
        }
    }

    fun getApiSubmittersByName(names: Collection<String>): Stream<DbSubmitterDto> {
        return databaseContext.inTransaction {
            it.attach(SubmitterDao::class.java).getApiSubmittersByName(names)
        }
    }

    fun getSubmitterForSubmission(submissionId: Int): DbSubmitterDto? {
        return databaseContext.inTransaction {
            it.attach(SubmitterDao::class.java).getSubmitterForSubmission(submissionId)
        }
    }

    //TODO Should be submitter model as parameter
    fun insertSubmitter(name: String, type: String): DbSubmitterDto {
        return databaseContext.inTransaction {
            val submitterDao = it.attach(SubmitterDao::class.java)

            if (submitterDao.getSubmitterByName(name) != null) {
                throw DuplicateSubmitterException()
            }

            return@inTransaction submitterDao.insertSubmitter(name, type)
        }
    }
}