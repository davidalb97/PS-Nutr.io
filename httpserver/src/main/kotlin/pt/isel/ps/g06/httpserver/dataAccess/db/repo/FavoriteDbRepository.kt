package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.FavoriteDao
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import java.lang.Exception

@Repository
class FavoriteDbRepository(jdbi: Jdbi): BaseDbRepo(jdbi) {

    fun getFavorite(
            submissionId: Int,
            userId: Int?,
            isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE
    ): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) { handle ->
            return@inTransaction userId?.let {
                handle.attach(FavoriteDao::class.java).getByIds(submissionId, userId)?.let { true }
            } ?: false
        }
    }
}