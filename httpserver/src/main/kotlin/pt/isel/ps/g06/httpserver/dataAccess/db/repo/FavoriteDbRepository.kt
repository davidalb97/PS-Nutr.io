package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.FavoriteDao

@Repository
class FavoriteDbRepository(private val databaseContext: DatabaseContext) {

    fun getFavorite(submissionId: Int, userId: Int): Boolean {
        return databaseContext.inTransaction { handle ->
            handle.attach(FavoriteDao::class.java).getByIds(submissionId, userId)?.let { true } ?: false
        }
    }
}