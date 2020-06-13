package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.FavoriteDao

@Repository
class FavoriteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

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